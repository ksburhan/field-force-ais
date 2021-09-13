import sys

from enum import IntEnum

from board.consumable import ALL_CONSUMABLES, Consumable
from board.fire import Fire
from board.wall import Wall
from game import gameconstants
from game.move import MoveType, Direction, Move
from game.player import Player
from game.skill import ALL_SKILLS, Skill, SkillType


class ClientPackets(IntEnum):
    PLAYERNAME = 1
    MOVEREPLY = 6


class ServerPackets(IntEnum):
    GAMEMODE = 2
    PLAYERINFORMATION = 3
    GAMEFIELD = 4
    MOVEREQUEST = 5
    NEWGAMESTATE = 7
    MOVEDISTRIBUTION = 8
    ERROR = 9
    GAMEOVER = 10


class Packet:

    def __init__(self, data):
        self.buffer = bytearray(data)
        self.readPos = int(0)

    # reads 4 bytes out of the byte array at index readpos and increases readpos by 4
    def read_int(self):
        if sys.getsizeof(self.buffer) > self.readPos:
            integer = int.from_bytes(
                [self.buffer[self.readPos], self.buffer[self.readPos + 1], self.buffer[self.readPos + 2],
                 self.buffer[self.readPos + 3]], byteorder='little', signed=True)
            self.readPos += 4
            return integer
        else:
            sys.exit("Couldn't read int")

    # read int first for length of string, then read remaining string
    def read_string(self):
        length = self.read_int()
        stringbuff = self.buffer[self.readPos:self.readPos+length]
        self.readPos += length
        return stringbuff.decode('utf-8')

    # read config, needed to play the game
    # then reads all consumables and skills available in the config
    def read_config(self):
        gameconstants.HP = self.read_int()
        gameconstants.SHIELD = self.read_int()

        gameconstants.ATTACK_DAMAGE = self.read_int()
        gameconstants.WALK_IN_PLAYER_DAMAGE = self.read_int()
        gameconstants.PLAYER_WALKED_INTO_DAMAGE = self.read_int()

        gameconstants.FIRE_DURATION_ON_MAP = self.read_int()
        gameconstants.ON_FIRE_EFFECT_DURATION = self.read_int()
        gameconstants.ON_FIRE_DAMAGE = self.read_int()

        gameconstants.WALL_HP = self.read_int()
        gameconstants.WALK_IN_WALL_DAMAGE = self.read_int()
        gameconstants.WALL_TAKE_DAMAGE = self.read_int()

        self.read_config_consumables()

        self.read_config_skills()

    # read config consumables and adds them to global variable
    def read_config_consumables(self):
        consum_count = self.read_int()
        for i in range(consum_count):
            c = chr(self.read_int())
            c_name = self.read_string()
            healing = self.read_int()
            shield = self.read_int()
            ALL_CONSUMABLES.append(Consumable(c, c_name, healing, shield, x=-1, y=-1))
            gameconstants.VALID_CONSUMABLES.append(c)

    # read config skills and adds them to global variable
    def read_config_skills(self):
        skill_count = self.read_int()
        for i in range(skill_count):
            identifier = self.read_int()
            s_name = self.read_string()
            cooldown = self.read_int()
            _range = self.read_int()
            value = self.read_int()
            skilltype = SkillType(self.read_int())
            ALL_SKILLS.append(Skill(identifier, s_name, cooldown, _range, value, skilltype, cooldown_left=0))

    # read all player information and returns list with player objects
    def read_players(self):
        player_count = self.read_int()
        players = []
        for i in range(player_count):
            playernumber = self.read_int()
            playername = self.read_string()
            hp = self.read_int()
            shield = self.read_int()
            xpos = self.read_int()
            ypos = self.read_int()
            skill1 = self.read_skill()
            skill2 = self.read_skill()
            players.append(Player(playernumber, playernumber, playername, hp, shield, xpos, ypos, skill1, skill2))
        return players

    # reads a single skill
    def read_skill(self):
        skillid = self.read_int()
        if skillid == -1:
            return None
        cooldown_left = self.read_int()
        skill = ALL_SKILLS[skillid]
        return Skill(skillid, skill.name, skill.cooldown, skill.range, skill.value, skill.skilltype, cooldown_left=cooldown_left)

    # reads the chars for a 2d map
    # reads dimension * dimension ints
    def read_map(self, dimension):
        in_map = [[-1 for y in range(dimension)] for x in range(dimension)]
        for y in range(dimension):
            for x in range(dimension):
                in_map[x][y] = chr(self.read_int())
        return in_map

    # read list for player order
    # first index is player next to move
    def read_player_in_turn(self):
        player_in_turn = []
        player_count = self.read_int()
        for i in range(player_count):
            player_in_turn.append(self.read_int())
        return player_in_turn

    # reads a single move
    def read_move(self):
        movetype = MoveType(self.read_int())
        direction = Direction(self.read_int())
        skill = self.read_skill()
        return Move(movetype, direction, skill)

    # reads all fires in the current gamestate
    def read_fires(self):
        fires = []
        fires_count = self.read_int()
        for i in range(fires_count):
            x = self.read_int()
            y = self.read_int()
            duration = self.read_int()
            fires.append(Fire('f', x, y, duration))
        return fires

    # reads all walls in the current gamestate
    def read_walls(self):
        walls = []
        walls_count = self.read_int()
        for i in range(walls_count):
            x = self.read_int()
            y = self.read_int()
            hp = self.read_int()
            walls.append(Wall('-', x, y, hp))
        return walls

    # reads all consumables in the current gamestate
    def read_consumables(self):
        consumables = []
        consumables_count = self.read_int()
        for i in range(consumables_count):
            identifier = chr(self.read_int())
            x = self.read_int()
            y = self.read_int()
            name = ''
            healing = 0
            shield = 0
            for c in ALL_CONSUMABLES:
                if c.identifier == identifier:
                    name = c.name
                    healing = c.healing
                    shield = c.shield
            consumables.append(Consumable(identifier, name, healing, shield, x, y))
        return consumables

    # writes an int as bytesto the bytearray
    def write_int(self, value):
        self.buffer.extend(value.to_bytes(4, byteorder='little', signed='unsigned'))

    # writes the lenght of a string and the string as bytes to the bytearray
    def write_string(self, value):
        self.buffer.extend(len(value).to_bytes(4, byteorder='little'))
        self.buffer.extend(value.encode())

    # writes a move object as bytes to the bytearray
    def write_move(self, move):
        self.write_int(int(move.type))
        self.write_int(int(move.direction))
        self.write_skill(move.skill)

    # writes a skill object as bytes, mainly just the id because the server tracks the rest
    def write_skill(self, skill):
        if skill is not None:
            self.write_int(skill.identifier)
        else:
            self.write_int(-1)

    # prepends the length of a packet for appropriate form
    def write_length(self):
        self.buffer[0:0] = len(self.buffer).to_bytes(4, byteorder='little')



