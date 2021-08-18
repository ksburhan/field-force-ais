import getopt
import os
import sys

import ai.ai
from connection.client import Client
from connection import clientsend, clienthandle
from connection.packet import Packet, ServerPackets

ip = "127.0.0.1"
port = 26353
playername = "dqn"
skill1 = 0
skill2 = 1

game_on = False


def parse(argv):
    try:
        opts, args = getopt.getopt(argv, "i:p:n:s1:s2:b:r:lvh",
                                   ["ip=", "port=", "name=", "skill1=", "skill2=", "brain=", "reward=", "learn", "verbose", "help"])
    except getopt.error as err:
        print(str(err))
        sys.exit(2)
    for current_argument, current_value in opts:
        if current_argument in ("-i", "--ip"):
            global ip
            ip = current_value
        elif current_argument in ("-p", "--port"):
            global port
            port = current_value
        elif current_argument in ("-n", "--playername"):
            global playername
            playername = current_value
        elif current_argument in ("-s1", "--skill1"):
            global skill1
            skill1 = current_value
        elif current_argument in ("-s2", "--skill2"):
            global skill2
            if skill2 == skill1:
                print("You can't have the same skill twice")
                exit(-1)
            skill2 = current_value
        elif current_argument in ("-b", "--brain"):
            ai.ai.model_path = current_value
        elif current_argument in ("-r", "--reward"):
            ai.ai.reward_path = current_value
        elif current_argument in ("-l", "--learn"):
            ai.ai.train = True
        elif current_argument in ("-v", "--verbose"):
            sys.stdout = open(os.devnull, 'w')
        elif current_argument in ("-h", "--help"):
            print("-i <ip>                 Set ip to connect to\n" +
                  "-p <port>               Set port to connect to\n" +
                  "-n <playername>         Set playername\n" +
                  "-s1 <skill_id>          Set skill 1\n" +
                  "-s2 <skill_id>          Set skill 2\n" +
                  "-b <*.pth path>         Set path to model\n" +
                  "-r <reward path>        Set path for reward file\n" +
                  "-l                      Set if the model should train\n" +
                  "-v                      Set verbose on\n" +
                  "-h                      Show help")
            exit(1)


def handle_msg(msgtype, packet, client):
    enum_type = ServerPackets(msgtype)
    switcher = {
        ServerPackets.GAMEMODE: lambda: clienthandle.handle_gamemode(packet),
        ServerPackets.PLAYERINFORMATION: lambda: clienthandle.handle_playerinformation(packet),
        ServerPackets.GAMEFIELD: lambda: clienthandle.handle_initialmap(packet),
        ServerPackets.MOVEREQUEST: lambda: clienthandle.handle_moverequest(packet, client),
        ServerPackets.NEWGAMESTATE: lambda: clienthandle.handle_newgamestate(packet),
        ServerPackets.MOVEDISTRIBUTION: lambda: clienthandle.handle_movedistribution(packet),
        ServerPackets.ERROR: lambda: clienthandle.handle_error(packet),
        ServerPackets.GAMEOVER: lambda: clienthandle.handle_gameover(packet)
    }
    func = switcher.get(enum_type, lambda: 'Invalid')
    func()


def main():
    parse(sys.argv[1:])
    client = Client(ip, port)
    client.connect()
    clientsend.send_playername(client, playername, skill1, skill2)

    global game_on
    game_on = True
    while game_on:
        length = int.from_bytes(client.client.recv(4), byteorder='little')
        msgtype = int.from_bytes(client.client.recv(4), byteorder='little')
        data = bytearray()
        while len(data) < length-4:
            data_rcv = client.client.recv(length - len(data) - 4)
            data.extend(data_rcv)
        packet = Packet(data)
        handle_msg(msgtype, packet, client)

    client.client.close()


if __name__ == '__main__':
    main()
