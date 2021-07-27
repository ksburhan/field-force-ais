from board.mapobject import MapObject


class Wall(MapObject):
    def __init__(self, identifier, x, y, hp):
        super().__init__(identifier, x, y)
        self.hp = hp

    def take_damage(self, damage, gamestate):
        self.hp -= damage
        if self.hp <= 0:
            self.destroy(gamestate)
