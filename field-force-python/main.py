import getopt
import os
import sys

from connection.client import Client
from connection import clientsend

ip = "127.0.0.1"
port = 26353
name = "python"
skill1 = 0
skill2 = 1

global client

def parse(argv):
    try:
        opts, args = getopt.getopt(argv, "i:p:n:s1:s2:vh",
                                   ["ip=", "port=", "name=", "skill1=", "skill2=", "verbose", "help"])
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
        elif current_argument in ("-n", "--name"):
            global name
            name = current_value
        elif current_argument in ("-s1", "--skill1"):
            global skill1
            skill1 = current_value
        elif current_argument in ("-s2", "--skill2"):
            global skill2
            if skill2 == skill1:
                print("You can't have the same skill twice")
                exit(-1)
            skill2 = current_value
        elif current_argument in ("-v", "--verbose"):
            sys.stdout = open(os.devnull, 'w')
        elif current_argument in ("-h", "--help"):
            print("-i <ip>                 Set ip to connect to\n" +
                  "-p <port>               Set port to connect to\n" +
                  "-n <name>               Set playername\n" +
                  "-s1 <skill_id>          Set skill 1\n" +
                  "-s2 <skill_id>          Set skill 2\n" +
                  "-v                      Set verbose on\n" +
                  "-h                      Show help")
            exit(1)


if __name__ == '__main__':
    parse(sys.argv[1:])
    client = Client(ip, port)
    client.connect()
    clientsend.send_playername()

