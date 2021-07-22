from game import gameconstants
from game.move import MoveType, Move, Direction

PLAYERS_IN_GAME = []


def is_valid_target(c):
    for x in gameconstants.VALID_TARGETS:
        return x == c


class GameState:
    def __init__(self, gamefield, players, playerinturn, fires, walls, consumables):
        self.gamefield = gamefield
        self.players = players
        self.playerinturn = playerinturn
        self.fires = fires
        self.walls = walls
        self.consumables = consumables

    def get_all_moves(self, player_id):
        moves = []
        player = self.players[player_id - 1]
        tile = self.gamefield.map[(player.x, player.y)]
        for m in MoveType:
            if m == MoveType.MOVEMENT:
                if tile.nTile is not None:
                    moves.append(Move(MoveType.MOVEMENT, Direction.NORTH, None))
                if tile.eTile is not None:
                    moves.append(Move(MoveType.MOVEMENT, Direction.EAST, None))
                if tile.sTile is not None:
                    moves.append(Move(MoveType.MOVEMENT, Direction.SOUTH, None))
                if tile.wTile is not None:
                    moves.append(Move(MoveType.MOVEMENT, Direction.WEST, None))
            elif m == MoveType.ATTACK:
                if tile.nTile is not None and is_valid_target(tile.nTile.content):
                    moves.append(Move(MoveType.ATTACK, Direction.NORTH, None))
                if tile.eTile is not None and is_valid_target(tile.eTile.content):
                    moves.append(Move(MoveType.ATTACK, Direction.EAST, None))
                if tile.sTile is not None and is_valid_target(tile.sTile.content):
                    moves.append(Move(MoveType.ATTACK, Direction.SOUTH, None))
                if tile.wTile is not None and is_valid_target(tile.wTile.content):
                    moves.append(Move(MoveType.ATTACK, Direction.WEST, None))
            elif m == MoveType.SKILL:
                for d in Direction:
                    if player.skill1 is not None and player.skill1.cooldown_left == 0:
                        moves.append(Move(MoveType.SKILL, d, player.skill1))
                    if player.skill2 is not None and player.skill2.cooldown_left == 0:
                        moves.append(Move(MoveType.SKILL, d, player.skill2))
        return moves
