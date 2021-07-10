class MapObject:
    def __init__(self, identifier, x, y):
        self.identifier = identifier
        self.x = x
        self.y = y

    def set_pos(self, x, y):
        self.x = x
        self.y = y
