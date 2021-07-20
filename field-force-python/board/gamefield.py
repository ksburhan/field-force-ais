from board.fire import Fire
from board.mapobject import MapObject
from board.tile import Tile
from board.wall import Wall
from game import gamestate


class GameField:
    def __init__(self, dimension, _map):
        self.dimension = dimension
        self.map = self.create_field(_map)

    def create_field(self, _map):
        tilemap = {}
        for y in range(self.dimension):
            for x in range(self.dimension):
                map_object = MapObject
                if _map[x][y] == '1':
                    map_object = gamestate.PLAYERS_IN_GAME[0]
                    map_object.set_pos(x, y)
                elif _map[x][y] == '2':
                    map_object = gamestate.PLAYERS_IN_GAME[1]
                    map_object.set_pos(x, y)
                elif _map[x][y] == '3':
                    map_object = gamestate.PLAYERS_IN_GAME[2]
                    map_object.set_pos(x, y)
                elif _map[x][y] == '4':
                    map_object = gamestate.PLAYERS_IN_GAME[3]
                    map_object.set_pos(x, y)
                elif _map[x][y] == 'f':
                    map_object = Fire('f', x, y, 2)
                elif _map[x][y] == '-':
                    map_object = Wall('-', x, y, 2)
                else:
                    map_object = MapObject(_map[x][y], x, y)
                tilemap[(x, y)] = Tile(x, y, map_object)
        return tilemap
