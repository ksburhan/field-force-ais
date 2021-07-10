from enum import IntEnum

ALL_SKILLS = []


class SkillType(IntEnum):
    MOVEMENT = 0
    REGENERATE = 1
    FIRE = 2
    ROCKET = 3
    PUSH = 4
    BREAK = 5


class Skill:
    def __init__(self, identifier, name, cooldown, _range, value, skilltype, cooldown_left):
        self.identifier = identifier
        self.name = name
        self.cooldown = cooldown
        self.range = _range
        self.value = value
        self.skilltype = skilltype
        self.cooldown_left = cooldown_left

    def set_cd(self):
        self.cooldown_left = self.cooldown
