import ai.ai
import main

from connection.packet import *
from game.move import MoveType


# prepends lenght of packet and sends it to the server
def send_packet(client, packet):
    packet.write_length()
    client.client.send(packet.buffer)


# sends Pakettype 1. tells server that client ist playing and what name and skills were chosen
def send_playername(client, playername, skill1, skill2):
    packet = Packet(0)
    packet.write_int(int(ClientPackets.PLAYERNAME))
    packet.write_string(playername)
    packet.write_int(skill1)
    packet.write_int(skill2)
    send_packet(client, packet)


# sends Pakettype 6. tells server the movereply and sets skills on cooldown if used
def send_movereply(client, playernumber, move):
    packet = Packet(0)
    packet.write_int(int(ClientPackets.MOVEREPLY))
    packet.write_int(int(playernumber))
    packet.write_move(move)
    if move.type == MoveType.SKILL:
        move.skill.set_cd()
        if move.skill.identifier == ai.ai.skill1:
            ai.ai.ownplayerobj.skill1.set_cd()
        if move.skill.identifier == ai.ai.skill2:
            ai.ai.ownplayerobj.skill2.set_cd()
    send_packet(client, packet)
