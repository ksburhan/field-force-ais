import gym
from gym import spaces
import numpy as np

import ai.ai
from game.gamestate import GameState
from game.move import Move, Direction, MoveType
from game.player import Player
from sklearn import preprocessing

# action space
# WALK_NORTH = 0
# WALK_EAST = 1
# WALK_SOUTH = 2
# WALK_WEST = 3
# ATTACK_NORTH = 4
# ATTACK_EAST = 5
# ATTACK_SOUTH = 6
# ATTACK_WEST = 7
# SKILL1_NORTH = 8
# SKILL1_EAST = 9
# SKILL1_SOUTH = 10
# SKILL1_WEST = 11
# SKILL2_NORTH = 12
# SKILL2_EAST = 13
# SKILL2_SOUTH = 14
# SKILL2_WEST = 15


# observation space
# mapobjects coded
# '0' = 0
# '1' = 1
# '2' = 2
# '3' = 3
# '4' = 4
# 'h' = 5
# 's' = 6
# 'm' = 7
# 'f' = 8
# '-' = 9
# 'x' = 10
def get_code(map_char):
    if map_char == '0':
        return 0
    elif map_char == '1':
        return 1
    elif map_char == '2':
        return 2
    elif map_char == '3':
        return 3
    elif map_char == '4':
        return 4
    elif map_char == 'h':
        return 5
    elif map_char == 's':
        return 6
    elif map_char == 'm':
        return 7
    elif map_char == 'f':
        return 8
    elif map_char == '-':
        return 9
    elif map_char == 'x':
        return 10
    else:
        return 11


class FieldEnv(gym.Env):
    def __init__(self):
        self.first_gamestate = GameState
        self.gamestate = GameState
        self.action_space = spaces.Discrete(16)
        self.observation_space = spaces.Box(np.array([0, 0, 0, 0]), np.array([1, 1, 1, 1]),
                                            dtype=np.int)
        dimension = ai.ai.current_gamestate.gamefield.dimension
        self.observation_space = spaces.Box(low=0, high=1, shape=(dimension * dimension + 4, 1), dtype=np.float32)

    def set_gamestate(self, gamestate):
        self.first_gamestate = gamestate
        self.gamestate = gamestate

    def reset(self):
        return self.observe()

    def step(self, action):
        move = Move
        if action == 0:
            move = Move(MoveType.MOVEMENT, Direction.NORTH, None)
        elif action == 1:
            move = Move(MoveType.MOVEMENT, Direction.EAST, None)
        elif action == 2:
            move = Move(MoveType.MOVEMENT, Direction.SOUTH, None)
        elif action == 3:
            move = Move(MoveType.MOVEMENT, Direction.WEST, None)
        elif action == 4:
            move = Move(MoveType.ATTACK, Direction.NORTH, None)
        elif action == 5:
            move = Move(MoveType.ATTACK, Direction.EAST, None)
        elif action == 6:
            move = Move(MoveType.ATTACK, Direction.SOUTH, None)
        elif action == 7:
            move = Move(MoveType.ATTACK, Direction.WEST, None)
        elif action == 8:
            move = Move(MoveType.SKILL, Direction.NORTH, ai.ai.ownplayerobj.skill1)
        elif action == 9:
            move = Move(MoveType.SKILL, Direction.EAST, ai.ai.ownplayerobj.skill1)
        elif action == 10:
            move = Move(MoveType.SKILL, Direction.SOUTH, ai.ai.ownplayerobj.skill1)
        elif action == 11:
            move = Move(MoveType.SKILL, Direction.WEST, ai.ai.ownplayerobj.skill1)
        elif action == 12:
            move = Move(MoveType.SKILL, Direction.NORTH, ai.ai.ownplayerobj.skill2)
        elif action == 13:
            move = Move(MoveType.SKILL, Direction.EAST, ai.ai.ownplayerobj.skill2)
        elif action == 14:
            move = Move(MoveType.SKILL, Direction.SOUTH, ai.ai.ownplayerobj.skill2)
        elif action == 15:
            move = Move(MoveType.SKILL, Direction.WEST, ai.ai.ownplayerobj.skill2)

        obs_ = self.observe()
        self.gamestate.simulate_next_gamestate(ai.ai.ownplayerobj.identifier, move)

        done = self.gamestate.is_game_over()
        obs = self.observe()

        reward = self.reward(obs_, obs)

        info = {}

        return obs, reward, done, info

    def reward(self, obs_, obs):
        reward = 0
        dehp = obs_[0] - obs[0]
        dohp = (obs_[1] - obs[1]) * (-1)
        reward = 10 * dehp + 10 * dohp
        return reward

    def observe(self):
        # hp enemies, own hp, skill1 cd, skill2 cd, mapobjects coded
        dimension = self.gamestate.gamefield.dimension
        ret = [-1 for i in range(4 + 12 * 12)]
        player_own = Player
        hp = 0
        for p in self.gamestate.players:
            if p.identifier == ai.ai.ownplayerobj.identifier:
                player_own = p
                continue
            hp = hp + p.hp + p.shield
        ret[0] = hp
        ret[1] = player_own.hp + player_own.shield
        ret[2] = player_own.skill1.cooldown_left
        ret[3] = player_own.skill2.cooldown_left

        offset = 0
        for y in range(dimension):
            for x in range(dimension):
                ret[4 + offset + (dimension * y + x)] = get_code(self.gamestate.gamefield.field_chars[x][y])
                if x == dimension-1:
                    offset = offset + (12 - dimension)

        return tuple(preprocessing.normalize([ret]))[0]

    def render(self, mode='human'):
        pass
