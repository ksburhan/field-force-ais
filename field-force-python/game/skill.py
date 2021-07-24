from enum import IntEnum

from board.consumable import Consumable
from board.fire import Fire
from board.tile import Tile
from board.wall import Wall
from game import gameconstants
from game.move import Direction
from game.player import Player

ALL_SKILLS = []


class SkillType(IntEnum):
    MOVEMENT = 0
    REGENERATE = 1
    FIRE = 2
    ROCKET = 3
    PUSH = 4
    BREAK = 5


class Skill:
    def __init__(self, identifier, name, cooldown, _range, value, skilltype, cooldown_left):
        self.identifier = identifier
        self.name = name
        self.cooldown = cooldown
        self.range = _range
        self.value = value
        self.skilltype = skilltype
        self.cooldown_left = cooldown_left

    def set_cd(self):
        self.cooldown_left = self.cooldown

    def prepare_for_next_round(self):
        if self.cooldown > 0:
            self.cooldown -= 1

    def use_skill(self, player, direction, gamestate):
        enum_type = SkillType(self.skilltype)
        switcher = {
            SkillType.MOVEMENT: lambda: self.movement_type(player, direction, gamestate),
            SkillType.REGENERATE: lambda: self.regenerate_type(player, direction, gamestate),
            SkillType.FIRE: lambda: self.fire_type(player, direction, gamestate),
            SkillType.ROCKET: lambda: self.rocket_type(player, direction, gamestate),
            SkillType.PUSH: lambda: self.push_type(player, direction, gamestate),
            SkillType.BREAK: lambda: self.break_type(player, direction, gamestate),
        }
        func = switcher.get(enum_type, lambda: 'Invalid')
        func()
        self.set_cd()

    def movement_type(self, player, direction, gamestate):
        for i in range(self.range):
            tile = gamestate.gamefield.map[(player.x, player.y)]
            if direction == Direction.NORTH:
                if tile.nTile is not None:
                    gamestate.move_to_tile(player, player.x, player.y - 1)
                else:
                    return
            elif direction == Direction.EAST:
                if tile.eTile is not None:
                    gamestate.move_to_tile(player, player.x + 1, player.y)
                else:
                    return
            elif direction == Direction.SOUTH:
                if tile.sTile is not None:
                    gamestate.move_to_tile(player, player.x, player.y + 1)
                else:
                    return
            elif direction == Direction.WEST:
                if tile.wTile is not None:
                    gamestate.move_to_tile(player, player.x - 1, player.y)
                else:
                    return

    def regenerate_type(self, player, direction, gamestate):
        player.heal(self.value)

    def fire_type(self, player, direction, gamestate):
        x_target = player.x
        y_target = player.y
        tile = Tile
        for i in range(self.range):
            tile = gamestate.gamefield.map[(x_target, y_target)]
            if direction == Direction.NORTH:
                if tile.nTile is not None:
                    y_target -= i
                else:
                    return
            elif direction == Direction.EAST:
                if tile.eTile is not None:
                    x_target += i
                else:
                    return
            elif direction == Direction.SOUTH:
                if tile.sTile is not None:
                    y_target += i
                else:
                    return
            elif direction == Direction.WEST:
                if tile.wTile is not None:
                    x_target -= i
                else:
                    return
            target_object = gamestate.gamefield.map[(x_target, y_target)].content
            if target_object.identifier == '0' or isinstance(target_object, Fire) or isinstance(target_object, Consumable):
                gamestate.gamefield.field_chars[x_target][y_target] = 'f'
                gamestate.gamefield.map[(x_target, y_target)].content = Fire('f', x_target, y_target, gameconstants.FIRE_DURATION_ON_MAP)
            elif isinstance(target_object, Player):
                target_object.take_damage(self.value, gamestate)
            elif isinstance(target_object, Wall):
                return

    def rocket_type(self, player, direction, gamestate):
        targets = [[-1, -1], [-1, -1], [-1, -1]]
        if direction == Direction.NORTH:
            targets[0][0] = player.x
            targets[0][1] = player.y - self.range
        elif direction == Direction.EAST:
            targets[0][0] = player.x + self.range
            targets[0][1] = player.y
        elif direction == Direction.SOUTH:
            targets[0][0] = player.x
            targets[0][1] = player.y + self.range
        elif direction == Direction.WEST:
            targets[0][0] = player.x - self.range
            targets[0][1] = player.y
        if direction == Direction.NORTH or direction == Direction.SOUTH:
            targets[1][0] = targets[0][0] + 1
            targets[1][1] = targets[0][1]
            targets[2][0] = targets[0][0] - 1
            targets[2][1] = targets[0][1]
        elif direction == Direction.EAST or direction == Direction.WEST:
            targets[1][0] = targets[0][0]
            targets[1][1] = targets[0][1] + 1
            targets[2][0] = targets[0][0]
            targets[2][1] = targets[0][1] - 1
        for t in targets:
            if 0 <= t[0] < gamestate.gamefield.dimension and 0 <= t[1] < gamestate.gamefield.dimension:
                target_object = gamestate.gamefield.map[(t[0], t[1])].content
                if target_object.identifier == '0' or isinstance(target_object, Fire) or isinstance(target_object, Consumable):
                    gamestate.gamefield.field_chars[t[0]][t[1]] = 'f'
                    gamestate.gamefield.map[(t[0], t[1])].content = Fire('f', t[0], t[1], gameconstants.FIRE_DURATION_ON_MAP)
                elif isinstance(target_object, Player):
                    target_object.take_damage(self.value, gamestate)

    def push_type(self, player, direction, gamestate):
        x_target = 0
        y_target = 0
        if direction == Direction.NORTH:
            x_target = player.x
            y_target = player.y - self.range
        elif direction == Direction.EAST:
            x_target = player.x + self.range
            y_target = player.y
        elif direction == Direction.SOUTH:
            x_target = player.x
            y_target = player.y + self.range
        elif direction == Direction.WEST:
            x_target = player.x - self.range
            y_target = player.y
        if 0 <= x_target < gamestate.gamefield.dimension and 0 <= y_target < gamestate.gamefield.dimension:
            tile = gamestate.gamefield.map[(x_target, y_target)]
            if tile.nTile is not None:
                nTile = tile.nTile
                target_object = nTile.content
                if isinstance(target_object, Player) and nTile.nTile is not None:
                    gamestate.move_to_tile(target_object, target_object.x, target_object.y - 1)
            if tile.eTile is not None:
                eTile = tile.eTile
                target_object = eTile.content
                if isinstance(target_object, Player) and eTile.eTile is not None:
                    gamestate.move_to_tile(target_object, target_object.x + 1, target_object.y)
            if tile.sTile is not None:
                sTile = tile.sTile
                target_object = sTile.content
                if isinstance(target_object, Player) and sTile.sTile is not None:
                    gamestate.move_to_tile(target_object, target_object.x, target_object.y + 1)
            if tile.wTile is not None:
                wTile = tile.wTile
                target_object = wTile.content
                if isinstance(target_object, Player) and wTile.nTile is not None:
                    gamestate.move_to_tile(target_object, target_object.x - 1, target_object.y)

    def break_type(self, player, direction, gamestate):
        x_target = player.x
        y_target = player.y
        tile = Tile
        broke = False
        for i in range(self.range):
            tile = gamestate.gamefield.map[(x_target, y_target)]
            if direction == Direction.NORTH:
                if tile.nTile is not None:
                    y_target -= i
                else:
                    return
            elif direction == Direction.EAST:
                if tile.eTile is not None:
                    x_target += i
                else:
                    return
            elif direction == Direction.SOUTH:
                if tile.sTile is not None:
                    y_target += i
                else:
                    return
            elif direction == Direction.WEST:
                if tile.wTile is not None:
                    x_target -= i
                else:
                    return
            target_object = gamestate.gamefield.map[(x_target, y_target)].content
            if isinstance(target_object, Player):
                target_object.take_damage(self.value, gamestate)
            elif isinstance(target_object, Wall):
                if broke:
                    return
                target_object.take_damage(gameconstants.WALL_HP, gamestate)
                broke = True
