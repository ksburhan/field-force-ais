from board.gamefield import GameField
from connection import clientsend
from game import gameconstants, gamestate
from ai import ai
from game.gamestate import GameState
from .. import main


def handle_gamemode(packet):
    gameconstants.GAMEMODE = packet.read_int()
    gameconstants.TIME_LIMIT = packet.read_int()
    gameconstants.OWN_PLAYER_ID = packet.read_int()
    if gameconstants.GAMEMODE == 1:
        ai.skill1 = packet.read_int()
        ai.skill2 = packet.read_int()
    packet.read_config()


def handle_playerinformation(packet):
    gamestate.PLAYERS_IN_GAME = packet.read_players()
    ai.ownplayerobj = gamestate.PLAYERS_IN_GAME[gameconstants.OWN_PLAYER_ID-1]


def handle_initialmap(packet):
    dimension = packet.read_int()
    init_map = packet.read_map(dimension)
    player_in_turn = packet.read_player_in_turn()
    fires = packet.read_fires()
    walls = packet.read_walls()
    consumables = packet.read_consumables()
    ai.current_gamestate = GameState(GameField(dimension, init_map), gamestate.PLAYERS_IN_GAME, player_in_turn, fires, walls, consumables)


def handle_moverequest(packet, client):
    own_id = packet.read_int()
    move = ai.get_best_move()
    clientsend.send_movereply(client, own_id, move)


def handle_newgamestate(packet):
    dimension = packet.read_int()
    new_map = packet.read_map(dimension)
    players = packet.read_players()
    player_in_turn = packet.read_player_in_turn()
    fires = packet.read_fires()
    walls = packet.read_walls()
    consumables = packet.read_consumables()
    ai.current_gamestate = GameState(GameField(dimension, new_map), players, player_in_turn, fires, walls, consumables)


def handle_movedistribution(packet):
    last_player_id = packet.read_int()
    move = packet.read_move()
    log = packet.read_string()
    print(log)
    ai.current_gamestate.lastmove = move


def handle_error(packet):
    errormessage = packet.read_string()
    print(errormessage)



def handle_gameover(packet):
    message = packet.read_string()
    print(message)
    exit(0)
