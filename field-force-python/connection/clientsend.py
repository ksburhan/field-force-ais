import main

from connection.packet import *


def send_packet(client, packet):
    packet.write_length()
    client.client.send(packet.buffer)


def send_playername(client, playername, skill1, skill2):
    packet = Packet()
    packet.write_int(int(ClientPackets.PLAYERNAME))
    packet.write_string(playername)
    packet.write_int(skill1)
    packet.write_int(skill2)
    send_packet(client, packet)


def send_movereply(client, playernumber, move):
    packet = Packet()
    packet.write_int(int(ClientPackets.MOVEREPLY))
    packet.write_int(int(playernumber))
    packet.write_move(move)
    send_packet(client, packet)
