#pragma once
#include "mapobject.h"

class Tile
{
public:
	int x_pos = 0;
	int y_pos = 0;
	MapObject* content;

	Tile* n_tile = nullptr;
	Tile* e_tile = nullptr;
	Tile* s_tile = nullptr;
	Tile* w_tile = nullptr;

	Tile();
	Tile(int, int, MapObject*);

	void set_pos(int, int);

	~Tile();
};
