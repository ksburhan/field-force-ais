from board.mapobject import MapObject
from game import gameconstants


class Player(MapObject):

    def __init__(self, identifier, playernumber, name, hp, shield, x, y, skill1, skill2):
        super().__init__(identifier, x, y)
        self.playernumber = playernumber
        self.name = name
        self.hp = hp
        self.shield = shield
        self.skill1 = skill1
        self.skill2 = skill2
        self.active = True
        self.on_fire = 0

    def take_damage(self, damage, gamestate):
        if self.shield > 0:
            self.take_shield_damage(damage, gamestate)
        else:
            self.hp -= damage
            if self.hp <= 0:
                self.set_inactive(gamestate)

    def take_shield_damage(self, damage, gamestate):
        self.shield -= damage
        if self.shield <= 0:
            damage = self.shield * (-1)
            self.shield = 0
            self.take_damage(damage, gamestate)

    def heal(self, heal):
        self.hp += heal
        if self.hp > gameconstants.HP:
            self.hp = gameconstants.HP

    def charge_shield(self, charge):
        self.shield += charge
        if self.shield > gameconstants.SHIELD:
            self.shield = gameconstants.SHIELD

    def set_on_fire(self):
        self.on_fire = gameconstants.ON_FIRE_EFFECT_DURATION

    def set_inactive(self, gamestate):
        self.destroy(gamestate)
        self.hp = 0
        self.shield = 0
        self.active = False

    def prepare_for_next_round(self, gamestate):
        if self.on_fire > 0:
            self.take_damage(gameconstants.ON_FIRE_DAMAGE, gamestate)
            self.on_fire -= 1
        if self.skill1 is not None:
            self.skill1.prepare_for_next_round()
        if self.skill2 is not None:
            self.skill2.prepare_for_next_round()