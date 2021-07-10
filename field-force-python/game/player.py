from board.mapobject import MapObject


class Player(MapObject):
    def __init__(self, identifier, playernumber, name, hp, shield, x, y, skill1, skill2):
        super().__init__(identifier, x, y)
        self.playernumber = playernumber
        self.name = name
        self.hp = hp
        self.shield = shield
        self.skill1 = skill1
        self.skill2 = skill2
