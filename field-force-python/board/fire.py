from board.mapobject import MapObject


class Fire(MapObject):
    def __init__(self, identifier, x, y, duration):
        super().__init__(identifier, x, y)
        self.duration = duration
