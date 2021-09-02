#pragma once
#include "../game/player.h"
#include "../game/gamestate.h"

#include <string>

class AI
{
public:
	Player* own_player;
	GameState* current_gamestate;

	static AI& get_instance();
	Move get_best_move();
private:
	AI() {}
	
public:
	AI(AI const&) = delete;
	void operator=(AI const&) = delete;
};
