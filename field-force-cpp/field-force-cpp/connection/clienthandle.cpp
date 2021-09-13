#ifdef _WIN32
#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>
#ifdef _DEBUG
#ifndef DBG_NEW
#define DBG_NEW new ( _NORMAL_BLOCK , __FILE__ , __LINE__ )
#define new DBG_NEW
#endif
#endif  // _DEBUG
#endif
#include "clienthandle.h"
#include "clientsend.h"

#include <iostream>

#include "../ai/ai.h"
#include "../game/player.h"
#include "../game/gameconstants.h"
#include "../game/gamestate.h"
#include "../game/player.h"
#include "../board/gamefield.h"
#include "../board/consumable.h"
#include "../board/fire.h"
#include "../board/wall.h"

/**
 * \brief Reads Pakettype 2. Gamemode, Timelimit and own player number.
 * if gamemode 1, two random skills are assigned
 */
void handle_gamemode(Packet packet)
{
	int gamemode = packet.read_int();
	int timelimit = packet.read_int();
	int own_id = packet.read_int();
	if (gamemode == 1)
	{
		skill1 = packet.read_int();
		skill2 = packet.read_int();
	}
	GAME_MODE = gamemode;
	TIME_LIMIT = timelimit;
	OWN_PLAYER_ID = own_id;
	packet.read_config();
}

/**
 * \brief Reads Pakettype 3. information of all other players
 */
void handle_playerinformation(Packet packet)
{
	std::vector<Player> players = packet.read_players();
	ALL_PLAYERS = players;
	AI& ai = AI::get_instance();
	ai.own_player = &ALL_PLAYERS.at(OWN_PLAYER_ID-1);
	// TODO: SET OWN PLAYER OBJECT AND SKILLS | SHOULD BE FINE NOW
}

/**
 * \brief reads Pakettype 4. reads all information to create new gamestate object as the current state
 */
void handle_initial_map(Packet packet)
{
	const int dimension = packet.read_int();
	std::vector<std::vector<char>> map = packet.read_map(dimension);
	std::vector<int> player_in_turn = packet.read_player_in_turn();
	std::vector<Fire> fires = packet.read_fires();
	std::vector<Wall> walls = packet.read_walls();
	std::vector<Consumable> consumables = packet.read_consumables();
	AI& ai = AI::get_instance();
	ai.current_gamestate = new GameState(new GameField(dimension, map, &ALL_PLAYERS), ALL_PLAYERS, player_in_turn, fires, walls, consumables);
}

/**
 * \brief reads Pakettype 5. Only used to start looking for a move to reply with
 */
void handle_move_request(Packet packet)
{
	int own_id = packet.read_int();
	Move move = AI::get_instance().get_best_move();
	send_movereply(own_id, &move);
}

/**
 * \brief reads Pakettype 7. reads information needed to create new gamestate object after last move has been calculated
 */
void handle_new_gamestate(Packet packet)
{
	const int dimension = packet.read_int();
	std::vector<std::vector<char>> map = packet.read_map(dimension);
	std::vector<Player> current_players = packet.read_players();
	std::vector<int> player_in_turn = packet.read_player_in_turn();
	std::vector<Fire> fires = packet.read_fires();
	std::vector<Wall> walls = packet.read_walls();
	std::vector<Consumable> consumables = packet.read_consumables();
	AI& ai = AI::get_instance();
	delete ai.current_gamestate;
	ai.current_gamestate = new GameState(new GameField(dimension, map, &current_players), current_players, player_in_turn, fires, walls, consumables);
}

/**
 * \brief reads Pakettype 8. tells the last move that has been calculated
 */
void handle_movedistribution(Packet packet)
{
	int last_player_id = packet.read_int();
	Move* move = packet.read_move();
	std::string log = packet.read_string();
	std::cout << log << std::endl;
	AI& ai = AI::get_instance();
	delete ai.current_gamestate->last_move;
	ai.current_gamestate->last_move = move;
}

/**
 * \brief reads Pakettype 9. received when own player did wrong move, gets disqualified but connected
 */
void handle_error(Packet packet)
{
	std::string message = packet.read_string();
	std::cout << message << std::endl;
}

/**
 * \brief reads Pakettype 10. received when game is over. tells winner number
 */
void handle_gameover(Packet packet)
{
	std::string message = packet.read_string();
	int winnder_id = packet.read_int();
	bool won = false;
	if (winnder_id == OWN_PLAYER_ID)
		won = true;
	std::cout << message << " Won?: " << won << std::endl;
	AI& ai = AI::get_instance();
	delete ai.current_gamestate;
}

// when new server packets are created, just add new function to handle new packet