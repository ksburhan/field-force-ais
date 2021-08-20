#include "ai.h"

AI& AI::getInstance()
{
	static AI instance;

	return instance;
}

Move AI::getBestMove()
{
	std::vector<Move> moves = current_gamestate.getAllMoves(OWN_PLAYER_ID);
	return moves.at(std::rand() % moves.size() + 0);
}
