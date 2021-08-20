#include "ai.h"

AI& AI::getInstance()
{
	static AI instance;

	return instance;
}

Move AI::getBestMove()
{
	return Move(MoveType(1), Direction(1));
}
