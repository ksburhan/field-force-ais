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

AI& AI::getInstance()
{
	static AI instance;

	return instance;
}

Move AI::getBestMove()
{
	std::vector<Move> moves = current_gamestate->getAllMoves(OWN_PLAYER_ID);
	return moves.at(std::rand() % moves.size() + 0);
}
