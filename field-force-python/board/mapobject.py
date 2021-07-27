class MapObject:
    def __init__(self, identifier, x, y):
        self.identifier = identifier
        self.x = x
        self.y = y

    def set_pos(self, x, y):
        self.x = x
        self.y = y

    def destroy(self, gamestate):
        gamestate.gamefield.field_chars[self.x][self.y] = '0'
        gamestate.gamefield.map[(self.x, self.y)].content = MapObject('0', self.x, self.y)
