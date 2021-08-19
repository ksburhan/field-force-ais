#include "clienthandle.h"
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
	// TODO: SET OWN PLAYER OBJECT AND SKILLS
}

void handleInitialMap(Packet packet)
{
	const int dimension = packet.readInt();
	std::vector<std::vector<char>> map = packet.readMap(dimension);
	std::vector<int> player_in_turn = packet.readPlayerInTurn();
	std::vector<Fire> fires = packet.readFires();
	std::vector<Wall> walls = packet.readWalls();
	std::vector<Consumable> consumables = packet.readConsumables();
	// TODO: ASSIGN TO AI
	GameState(GameField(dimension, map), ALL_PLAYERS, player_in_turn, fires, walls, consumables);
}

void handleMoveRequest(Packet packet)
{

}

void handleNewGamestate(Packet packet)
{
	
}

void handleMovedistribution(Packet packet)
{
	
}

void handleError(Packet packet)
{
	
}

void handleGameover(Packet packet)
{
	
}
