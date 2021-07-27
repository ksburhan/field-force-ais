from enum import IntEnum


class Direction(IntEnum):
    NORTH = 1
    EAST = 2
    SOUTH = 3
    WEST = 4


class MoveType(IntEnum):
    MOVEMENT = 1
    ATTACK = 2
    SKILL = 3


class Move:
    def __init__(self, movetype, direction, skill):
        self.type = movetype
        self.direction = direction
        self.skill = skill


