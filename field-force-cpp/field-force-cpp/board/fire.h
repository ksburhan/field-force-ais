#pragma once
#include "mapobject.h"
#include "../game/gameconstants.h"

class GameState;

class Fire: public MapObject
{
public:
	int duration = FIRE_DURATION_ON_MAP;

	Fire();
	Fire(char, int, int);
	Fire(char, int, int, int);

	void prepare_for_next_round(GameState*);
};
