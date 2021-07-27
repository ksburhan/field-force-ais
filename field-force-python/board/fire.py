from board.mapobject import MapObject


class Fire(MapObject):
    def __init__(self, identifier, x, y, duration):
        super().__init__(identifier, x, y)
        self.duration = duration

    def prepare_for_next_round(self, gamestate):
        self.duration -= 1
        if self.duration <= 0:
            self.destroy(gamestate)
