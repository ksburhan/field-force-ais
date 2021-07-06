import main

from connection.packet import *
from main import client


def send_packet(packet):
    packet.write_length()
    client = main.client
    client.client.send(packet)


def send_playername():
    packet = Packet()
    packet.write_int(int(ClientPackets.PLAYERNAME))
    packet.write_string(main.name)
    packet.write_int(main.skill1)
    packet.write_int(main.skill2)
    send_packet(packet)


def send_movereply(id, move):
    packet = Packet()
    packet.write_int(int(ClientPackets.MOVEREPLY))
    packet.write_int(int(id))
    packet.write_move(move)
    send_packet(packet)


