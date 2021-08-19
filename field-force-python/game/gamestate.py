from board.consumable import Consumable
from board.fire import Fire
from board.mapobject import MapObject
from board.wall import Wall
from game import gameconstants
from game.move import MoveType, Move, Direction
from game.player import Player


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
        self.last_move = None

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

    def simulate_next_gamestate(self, player_id, move):
        player = self.players[player_id - 1]
        if move.type == MoveType.MOVEMENT:
            if move.direction == Direction.NORTH:
                self.move_to_tile(player, player.x, player.y - 1)
            if move.direction == Direction.EAST:
                self.move_to_tile(player, player.x + 1, player.y)
            if move.direction == Direction.SOUTH:
                self.move_to_tile(player, player.x, player.y + 1)
            if move.direction == Direction.WEST:
                self.move_to_tile(player, player.x - 1, player.y)
        if move.type == MoveType.ATTACK:
            if move.direction == Direction.NORTH:
                self.attack_tile(player, player.x, player.y - 1)
            if move.direction == Direction.EAST:
                self.attack_tile(player, player.x + 1, player.y)
            if move.direction == Direction.SOUTH:
                self.attack_tile(player, player.x, player.y + 1)
            if move.direction == Direction.WEST:
                self.attack_tile(player, player.x - 1, player.y)
        if move.type == MoveType.SKILL:
            skill = player.skill1
            if move.skill.identifier == player.skill2:
                skill = player.skill2
            skill.use_skill(player, move.direction, self)
        if player_id in self.playerinturn:
            self.playerinturn.remove(player_id)
            self.playerinturn.append(player_id)
        self.prepare_for_next_round()
        self.last_move = move

    def move_to_tile(self, player, x_target, y_target):
        target_object = self.gamefield.map[(x_target, y_target)].content
        x = player.x
        y = player.y
        if target_object.identifier == '0':
            self.gamefield.field_chars[x][y] = '0'
            self.gamefield.map[(x, y)].content = MapObject('0', x, y)
            self.gamefield.field_chars[x_target][y_target] = player.identifier
            self.gamefield.map[(x_target, y_target)].content = player
            player.set_pos(x_target, y_target)
        elif isinstance(target_object, Player):
            player.take_damage(gameconstants.WALK_IN_PLAYER_DAMAGE, self)
            target_object.take_damage(gameconstants.PLAYER_WALKED_INTO_DAMAGE, self)
        elif isinstance(target_object, Fire):
            self.gamefield.field_chars[x][y] = '0'
            self.gamefield.map[(x, y)].content = MapObject('0', x, y)
            self.gamefield.field_chars[x_target][y_target] = player.identifier
            self.gamefield.map[(x_target, y_target)].content = player
            player.set_pos(x_target, y_target)
            player.take_damage(gameconstants.ON_FIRE_DAMAGE, self)
            player.set_on_fire()
        elif isinstance(target_object, Wall):
            player.take_damage(gameconstants.WALK_IN_WALL_DAMAGE, self)
            target_object.take_damage(gameconstants.WALL_TAKE_DAMAGE, self)
        elif target_object.identifier == 'x':
            self.gamefield.field_chars[x][y] = '0'
            self.gamefield.map[(x, y)].content = MapObject('0', x, y)
            player.set_inactive(self)
            if player.identifier in self.playerinturn:
                self.playerinturn.remove(player.identifier)
        elif isinstance(target_object, Consumable):
            self.gamefield.field_chars[x][y] = '0'
            self.gamefield.map[(x, y)].content = MapObject('0', x, y)
            self.gamefield.field_chars[x_target][y_target] = player.identifier
            self.gamefield.map[(x_target, y_target)].content = player
            if target_object.healing > 0:
                player.heal(target_object.healing)
            else:
                player.take_damage(target_object.healing, self)
            if target_object.shield > 0:
                player.charge_shield(target_object.shield)

    def attack_tile(self, player, x_target, y_target):
        target_object = self.gamefield.map[(x_target, y_target)].content
        if isinstance(target_object, Player):
            target_object.take_damage(gameconstants.ATTACK_DAMAGE, self)
        elif isinstance(target_object, Wall):
            target_object.take_damage(gameconstants.ATTACK_DAMAGE, self)

    def prepare_for_next_round(self):
        for p in self.players:
            if p.active:
                p.prepare_for_next_round(self)
        for f in self.fires:
            f.prepare_for_next_round(self)

    def is_valid_move(self, move, player_id):
        player = self.players[player_id - 1]
        tile = self.gamefield.map[(player.x, player.y)]
        if move.type == MoveType.MOVEMENT:
            if tile.nTile is not None and move.direction == Direction.NORTH:
                return True
            if tile.eTile is not None and move.direction == Direction.EAST:
                return True
            if tile.sTile is not None and move.direction == Direction.SOUTH:
                return True
            if tile.wTile is not None and move.direction == Direction.WEST:
                return True
        elif move.type == MoveType.ATTACK:
            if tile.nTile is not None and is_valid_target(tile.nTile.content) and move.direction == Direction.NORTH:
                return True
            if tile.eTile is not None and is_valid_target(tile.eTile.content) and move.direction == Direction.EAST:
                return True
            if tile.sTile is not None and is_valid_target(tile.sTile.content) and move.direction == Direction.SOUTH:
                return True
            if tile.wTile is not None and is_valid_target(tile.wTile.content) and move.direction == Direction.WEST:
                return True
        elif move.type == MoveType.SKILL:
            if player.skill1.identifier == move.skill.identifier:
                if player.skill1 is not None and player.skill1.cooldown_left == 0:
                    return True
            elif player.skill2.identifier == move.skill.identifier:
                if player.skill2 is not None and player.skill2.cooldown_left == 0:
                    return True
        return False

    def is_game_over(self):
        return len(self.playerinturn) <= 1
