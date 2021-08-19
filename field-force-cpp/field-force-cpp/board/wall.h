#pragma once
#include "mapobject.h"
#include "../game/gameconstants.h"

class Wall: public MapObject
{
public:
	int hp = WALL_HP;

	Wall();
	Wall(char, int, int);
	Wall(char, int, int, int);
};
