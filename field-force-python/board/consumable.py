from board.mapobject import MapObject

ALL_CONSUMABLES = []


class Consumable(MapObject):
    def __init__(self, identifier, name, healing, shield, x, y):
        super().__init__(identifier, x, y)
        self.name = name
        self.healing = healing
        self.shield = shield
