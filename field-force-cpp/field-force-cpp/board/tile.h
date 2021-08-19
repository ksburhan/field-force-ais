#pragma once
#include "mapobject.h"

class Tile
{
public:
	int x_pos = 0;
	int y_pos = 0;
	MapObject* content = nullptr;

	Tile* nTile = nullptr;
	Tile* eTile = nullptr;
	Tile* sTile = nullptr;
	Tile* wTile = nullptr;

	Tile();
	Tile(int, int, MapObject*);
};
