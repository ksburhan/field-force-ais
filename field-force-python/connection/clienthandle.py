import time

import game.player
from board.gamefield import GameField
from connection import clientsend
from game import gameconstants, gamestate
from ai import ai
from game.gamestate import GameState


# Reads Pakettype 2. Gamemode, Timelimit and own player number
# if gamemode 1, two random skills are assigned
def handle_gamemode(packet):
    print("Type 2")
    gameconstants.GAMEMODE = packet.read_int()
    gameconstants.TIME_LIMIT = packet.read_int()
    gameconstants.OWN_PLAYER_ID = packet.read_int()
    if gameconstants.GAMEMODE == 1:
        ai.skill1 = packet.read_int()
        ai.skill2 = packet.read_int()
    packet.read_config()


# Reads Pakettype 3. information of all other players
def handle_playerinformation(packet):
    print("Type 3")
    game.player.PLAYERS_IN_GAME = packet.read_players()
    ai.ownplayerobj = game.player.PLAYERS_IN_GAME[gameconstants.OWN_PLAYER_ID - 1]


# reads Pakettype 4. reads all information to create new gamestate object as the current state
def handle_initialmap(packet):
    print("Type 4")
    dimension = packet.read_int()
    init_map = packet.read_map(dimension)
    player_in_turn = packet.read_player_in_turn()
    fires = packet.read_fires()
    walls = packet.read_walls()
    consumables = packet.read_consumables()
    ai.current_gamestate = GameState(GameField(dimension, init_map, game.player.PLAYERS_IN_GAME), game.player.PLAYERS_IN_GAME, player_in_turn, fires,
                                     walls, consumables)


# reads Pakettype 5. Only used to start looking for a move to reply with
def handle_moverequest(packet, client):
    print("Type 5")
    own_id = packet.read_int()
    ai.time_start = round(time.time() * 1000)
    move = ai.get_best_move()
    clientsend.send_movereply(client, own_id, move)


# reads Pakettype 7. reads information needed to create new gamestate object after last move has been calculated
def handle_newgamestate(packet):
    print("Type 7")
    dimension = packet.read_int()
    new_map = packet.read_map(dimension)
    players = packet.read_players()
    player_in_turn = packet.read_player_in_turn()
    fires = packet.read_fires()
    walls = packet.read_walls()
    consumables = packet.read_consumables()
    ai.current_gamestate = GameState(GameField(dimension, new_map, players), players, player_in_turn, fires, walls, consumables)
    ai.current_gamestate.gamefield.print_map()


# reads Pakettype 8. tells the last move that has been calculated
def handle_movedistribution(packet):
    print("Type 8")
    last_player_id = packet.read_int()
    move = packet.read_move()
    log = packet.read_string()
    print(log)
    ai.current_gamestate.lastmove = move


# reads Pakettype 9. received when own player did wrong move, gets disqualified but connected
def handle_error(packet):
    print("Type 9")
    errormessage = packet.read_string()
    print(errormessage)
    exit(9)


# reads Pakettype 10. received when game is over. tells winner number
def handle_gameover(packet):
    print("Type 10")
    message = packet.read_string()
    winner_id = packet.read_int()
    won = True if winner_id == ai.ownplayerobj.playernumber else False
    print(message)
    print(won)
    exit(10)


#
# when new server packets are created, just add new function to handle new packet
#
