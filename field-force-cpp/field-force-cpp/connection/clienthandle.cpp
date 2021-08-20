#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>
#ifdef _DEBUG
#ifndef DBG_NEW
#define DBG_NEW new ( _NORMAL_BLOCK , __FILE__ , __LINE__ )
#define new DBG_NEW
#endif
#endif  // _DEBUG
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

void handleGamemode(Packet packet)
{
	int gamemode = packet.readInt();
	int timelimit = packet.readInt();
	int own_id = packet.readInt();
	if (gamemode == 1)
	{
		skill1 = packet.readInt();
		skill2 = packet.readInt();
	}
	GAME_MODE = gamemode;
	TIME_LIMIT = timelimit;
	OWN_PLAYER_ID = own_id;
	packet.readConfig();
}

void handlePlayerinformation(Packet packet)
{
	std::vector<Player> players = packet.readPlayers();
	ALL_PLAYERS = players;
	AI& ai = AI::getInstance();
	ai.own_player = ALL_PLAYERS.at(OWN_PLAYER_ID-1);
	// TODO: SET OWN PLAYER OBJECT AND SKILLS | SHOULD BE FINE NOW
}

void handleInitialMap(Packet packet)
{
	const int dimension = packet.readInt();
	std::vector<std::vector<char>> map = packet.readMap(dimension);
	std::vector<int> player_in_turn = packet.readPlayerInTurn();
	std::vector<Fire> fires = packet.readFires();
	std::vector<Wall> walls = packet.readWalls();
	std::vector<Consumable> consumables = packet.readConsumables();
	AI& ai = AI::getInstance();
	ai.current_gamestate = GameState(GameField(dimension, map), ALL_PLAYERS, player_in_turn, fires, walls, consumables);
}

void handleMoveRequest(Packet packet)
{
	int own_id = packet.readInt();
	Move move = AI::getInstance().getBestMove();
	sendMovereply(own_id, move);
}

void handleNewGamestate(Packet packet)
{
	const int dimension = packet.readInt();
	std::vector<std::vector<char>> map = packet.readMap(dimension);
	std::vector<Player> current_players = packet.readPlayers();
	std::vector<int> player_in_turn = packet.readPlayerInTurn();
	std::vector<Fire> fires = packet.readFires();
	std::vector<Wall> walls = packet.readWalls();
	std::vector<Consumable> consumables = packet.readConsumables();
	AI& ai = AI::getInstance();
	ai.current_gamestate = GameState(GameField(dimension, map), current_players, player_in_turn, fires, walls, consumables);
}

void handleMovedistribution(Packet packet)
{
	int last_player_id = packet.readInt();
	Move move = packet.readMove();
	std::string log = packet.readString();
	std::cout << log << std::endl;
	AI& ai = AI::getInstance();
	ai.current_gamestate.last_move = move;
}

void handleError(Packet packet)
{
	std::string message = packet.readString();
	std::cout << message << std::endl;
}

void handleGameover(Packet packet)
{
	std::string message = packet.readString();
	int winnder_id = packet.readInt();
	bool won = false;
	if (winnder_id == OWN_PLAYER_ID)
		won = true;
	std::cout << message << " Won?: " << won << std::endl;
}
