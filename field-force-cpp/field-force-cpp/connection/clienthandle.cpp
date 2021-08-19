#include "clienthandle.h"
#include "../game/gameconstants.h"

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
	
}

void handleInitialMap(Packet packet)
{
	
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
