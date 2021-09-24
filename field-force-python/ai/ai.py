import copy
import random
import time

import game.gameconstants
from game import skill
from game.gamestate import GameState
from game.move import Move, MoveType, Direction
from game.player import Player
from game.skill import Skill

skill1 = -1
skill2 = -1

ownplayerobj = Player

current_gamestate = GameState

time_start = 0


# function that is supposed to be changed by players. currently returns a random valid move
# players should change this to return AI move output
def get_best_move():
    all_moves = current_gamestate.get_all_moves(ownplayerobj.identifier)
    try:
        check_time_limit()
        move = random.choice(all_moves)
        return move
    except Exception as e:
        print("time is running out!")
        return random.choice(all_moves)


def check_time_limit():
    if time.time() * 1000 - time_start > (game.gameconstants.TIME_LIMIT * 1000) - 500:
        raise Exception()
