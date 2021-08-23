import board
import game.player
from board import consumable
from board.consumable import Consumable
from board.fire import Fire
from board.mapobject import MapObject
from board.tile import Tile
from board.wall import Wall
from game import gamestate, gameconstants


class GameField:
    def __init__(self, dimension, _map, current_players):
        self.dimension = dimension
        self.map = self.create_field(_map, current_players)
        self.field_chars = _map

    def create_field(self, _map, current_players):
        tilemap = {}
        for y in range(self.dimension):
            for x in range(self.dimension):
                map_object = MapObject
                if _map[x][y] == '1':
                    map_object = self.get_player(1, current_players)
                    map_object.set_pos(x, y)
                elif _map[x][y] == '2':
                    map_object = self.get_player(2, current_players)
                    map_object.set_pos(x, y)
                elif _map[x][y] == '3':
                    map_object = self.get_player(3, current_players)
                    map_object.set_pos(x, y)
                elif _map[x][y] == '4':
                    map_object = self.get_player(4, current_players)
                    map_object.set_pos(x, y)
                elif _map[x][y] == 'f':
                    map_object = Fire('f', x, y, gameconstants.FIRE_DURATION_ON_MAP)
                elif _map[x][y] == '-':
                    map_object = Wall('-', x, y, gameconstants.WALL_HP)
                elif _map[x][y] in gameconstants.VALID_CONSUMABLES:
                    c = Consumable
                    for co in consumable.ALL_CONSUMABLES:
                        if co.identifier == _map[x][y]:
                            c = co
                            break
                    map_object = Consumable(_map[x][y], c.name, c.healing, c.shield, x, y)
                else:
                    map_object = MapObject(_map[x][y], x, y)
                tilemap[(x, y)] = Tile(x, y, map_object)
        for y in range(self.dimension):
            for x in range(self.dimension):
                if y > 0:
                    tilemap[(x, y)].nTile = tilemap[(x, y - 1)]
                if x is not self.dimension-1:
                    tilemap[(x, y)].eTile = tilemap[(x + 1, y)]
                if y is not self.dimension-1:
                    tilemap[(x, y)].sTile = tilemap[(x, y + 1)]
                if x > 0:
                    tilemap[(x, y)].wTile = tilemap[(x - 1, y)]
        return tilemap

    def print_map(self):
        for y in range(self.dimension):
            for x in range(self.dimension):
                print(self.map[(x, y)].content.identifier, end=' ')
            print()

    def get_player(self, number, current_players):
        for p in current_players:
            if p.playernumber == number:
                return p