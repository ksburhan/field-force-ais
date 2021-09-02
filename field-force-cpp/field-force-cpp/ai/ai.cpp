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
#include "ai.h"

#include <ctime>

AI& AI::get_instance()
{
	static AI instance;

	return instance;
}

Move AI::get_best_move()
{
	srand(time(NULL));
	std::vector<Move> moves = current_gamestate->get_all_moves(OWN_PLAYER_ID);
	Move move = moves.at(std::rand() % moves.size() + 0);
	current_gamestate->simulate_next_gamestate(OWN_PLAYER_ID, &move);
	
	return move;
}
