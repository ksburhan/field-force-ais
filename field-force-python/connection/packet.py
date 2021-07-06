import sys

from enum import IntEnum

from game.gameconstants import HPS


class ClientPackets(IntEnum):
    PLAYERNAME = 1
    MOVEREPLY = 6


class ServerPackets(IntEnum):
    GAMEMODE = 2
    PLAYERINFORMATION = 3
    GAMEFIELD = 4
    MOVEREQUEST = 5
    NEWGAMESTATE = 7
    MOVEDISTRIBUTION = 8
    ERROR = 9
    GAMEOVER = 10


class Packet:

    def __init__(self):
        self.buffer = bytearray()
        self.readPos = int(0)

    def read_int(self):
        if sys.getsizeof(self.buffer) > self.readPos:
            integer = int.from_bytes(
                [self.buffer[self.readPos], self.buffer[self.readPos + 1], self.buffer[self.readPos + 2],
                 self.buffer[self.readPos + 3]], byteorder='little')
            self.readPos += 4
            print(integer)
            return integer
        else:
            sys.exit("Couldn't read int")

    def read_config(self):
        HP = self.read_int()
        SHIELD = self.read_int()

    def write_int(self, value):
        self.buffer.extend(value.to_bytes(4, byteorder='big'))

    def write_string(self, value):
        self.buffer.extend(value.encode())

    def write_move(self, move):
        self.write_int(int(move.type))
        self.write_int(int(move.direction))
        self.write_int(int(move.skill))

    def write_skill(self, skill):
        if skill is not None:
            self.write_int(skill.id)
        else:
            self.write_int(-1)

    def write_length(self):
        self.buffer[0:0] = len(self.buffer).to_bytes(4, byteorder='big')



