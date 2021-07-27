import copy
import random

from game import skill
from game.gamestate import GameState
from game.move import Move, MoveType, Direction
from game.player import Player
from game.skill import Skill

skill1 = -1
skill2 = -1

ownplayerobj = Player

current_gamestate = GameState


def get_best_move():
    all_moves = current_gamestate.get_all_moves(ownplayerobj.identifier)
    move = random.choice(all_moves)
    return move
