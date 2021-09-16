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
	int enemy_distance(GameState*);
	int consum_distance(GameState*);

	Direction enemy_direction;
	Direction consum_direction;

public:
	AI(AI const&) = delete;
	void operator=(AI const&) = delete;
};
