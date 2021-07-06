from enum import IntEnum


class SkillType(IntEnum):
    MOVEMENT = 0
    REGENERATE = 1
    FIRE = 2
    ROCKET = 3
    PUSH = 4
    BREAK = 5


class Skill:
    def __init__(self, id, name, cooldown, range, value, skilltype):
        self.id = id
        self.name = name
        self.cooldown = cooldown
        self.range = range
        self.value = value
        self.skilltype = skilltype
