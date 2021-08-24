#pragma once
#include "../game/player.h"
#include "../game/gamestate.h"

#include <string>

class AI
{
public:
	Player* own_player;
	GameState* current_gamestate;

	static AI& getInstance();
	Move getBestMove();
private:
	AI() {}
	
public:
	AI(AI const&) = delete;
	void operator=(AI const&) = delete;
};
