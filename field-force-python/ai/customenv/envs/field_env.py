import gym
from gym import spaces
import numpy as np

import ai.ai
from game.gamestate import GameState
from game.move import Move, Direction, MoveType
from game.player import Player

WALK_NORTH = 0
WALK_EAST = 1
WALK_SOUTH = 2
WALK_WEST = 3
ATTACK_NORTH = 4
ATTACK_EAST = 5
ATTACK_SOUTH = 6
ATTACK_WEST = 7
SKILL1_NORTH = 8
SKILL1_EAST = 9
SKILL1_SOUTH = 10
SKILL1_WEST = 11
SKILL2_NORTH = 12
SKILL2_EAST = 13
SKILL2_SOUTH = 14
SKILL2_WEST = 15


class FieldEnv(gym.Env):
    def __init__(self):
        self.first_gamestate = GameState
        self.gamestate = GameState
        self.action_space = spaces.Discrete(16)
        self.observation_space = spaces.Box(np.array([0, 0, 0, 0, 0, 0]), np.array([10, 10, 10, 10, 10, 10]),
                                            dtype=np.int)

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
        ddist = obs_[0] - obs[0]
        dehp = obs_[1] - obs[1]
        dohp = (obs_[2] - obs[2]) * (-1)
        reward = 2 * ddist + dehp + 3 * dohp
        return reward

    def observe(self):
        # distance enemies, hp enemies, own hp, skill1 cd, skill2 cd, distance consumable
        ret = [0, 0, 0, 0, 0, 0]
        player_own = Player
        d = float('inf')
        for p in self.gamestate.players:
            if p.identifier == ai.ai.ownplayerobj.identifier:
                player_own = p
                continue
            dx = abs(p.x - ai.ai.ownplayerobj.x)
            dy = abs(p.y - ai.ai.ownplayerobj.y)
            dd = abs(dx + dy)
            if dd < d:
                d = dd
        ret[0] = d
        hp = 0
        for p in self.gamestate.players:
            if p.identifier == ai.ai.ownplayerobj.identifier:
                continue
            hp = hp + p.hp + p.shield
        ret[1] = hp
        ret[2] = player_own.hp + player_own.shield
        ret[3] = player_own.skill1.cooldown_left
        ret[4] = player_own.skill2.cooldown_left
        dc = float('inf')
        for c in self.gamestate.consumables:
            dx = abs(c.x - player_own.x)
            dy = abs(c.y - player_own.y)
            dd = abs(dx + dy)
            if dd < dc:
                dc = dd
        ret[5] = dc

        return tuple(ret)

    def render(self, mode='human'):
        pass
