import copy
import random
import torch
from torch import nn
from gym import Env
from gym.spaces import Discrete, Box

import main
from ai.agent import Agent
from ai.customenv.envs import FieldEnv
from game import skill
from game.gamestate import GameState
from game.move import Move, MoveType, Direction
from game.player import Player
from game.skill import Skill

import gym
from board.gamefield import GameField
from game.gamestate import GameState
import numpy as np
import test

skill1 = -1
skill2 = -1

ownplayerobj = Player

current_gamestate = GameState

model_path = 'model_weights.pth'
reward_path = 'rewards.txt'
train = False
GAMMA = 0.99
BATCH_SIZE = 32
BUFFER_SIZE = 50000
EPSILON_START = 1.0
EPSILON_END = 0.02
EPSILON_DECAY = 0.996
ALPHA = 0.001


class AIDQN:
    def __init__(self):
        self.env = FieldEnv()
        gamestate = copy.deepcopy(current_gamestate)
        self.env.set_gamestate(gamestate)
        self.agent = Agent(gamma=GAMMA, epsilon=EPSILON_START, batch_size=BATCH_SIZE, n_actions=16, eps_end=EPSILON_END,
                           input_dims=[4 + (12*12)], alpha=ALPHA, eps_dec=EPSILON_DECAY)
        self.scores, self.eps_history = [], []
        self.score = 0
        self.agent.load_model()


aidqn = AIDQN


def get_best_move():
    gamestate = copy.deepcopy(current_gamestate)
    aidqn.env.set_gamestate(gamestate)
    observation = aidqn.env.reset()
    action = aidqn.agent.choose_action(observation, False)
    while current_gamestate.is_valid_move(get_move(action), ownplayerobj.identifier) is not True:
        action = aidqn.agent.choose_action(observation, True)
    observation_, reward, done, info = aidqn.env.step(action)
    print('reward', reward)
    aidqn.score += reward
    aidqn.agent.store_transition(observation, action, reward,
                                 observation_, done)
    if train:
        aidqn.agent.learn()
    observation = observation_
    aidqn.scores.append(aidqn.score)
    aidqn.eps_history.append(aidqn.agent.EPSILON)
    mov = get_move(action)
    return mov


def get_move(action):
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
        move = Move(MoveType.SKILL, Direction.NORTH, ownplayerobj.skill1)
    elif action == 9:
        move = Move(MoveType.SKILL, Direction.EAST, ownplayerobj.skill1)
    elif action == 10:
        move = Move(MoveType.SKILL, Direction.SOUTH, ownplayerobj.skill1)
    elif action == 11:
        move = Move(MoveType.SKILL, Direction.WEST, ownplayerobj.skill1)
    elif action == 12:
        move = Move(MoveType.SKILL, Direction.NORTH, ownplayerobj.skill2)
    elif action == 13:
        move = Move(MoveType.SKILL, Direction.EAST, ownplayerobj.skill2)
    elif action == 14:
        move = Move(MoveType.SKILL, Direction.SOUTH, ownplayerobj.skill2)
    elif action == 15:
        move = Move(MoveType.SKILL, Direction.WEST, ownplayerobj.skill2)
    return move
