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
#include "tile.h"

Tile::Tile() { }

Tile::Tile(int _x_pos, int _y_pos, MapObject* _content)
{
	x_pos = _x_pos;
	y_pos = _y_pos;
	content = _content;
}

void Tile::set_pos(int _x_pos, int _y_pos)
{
	x_pos = _x_pos;
	y_pos = _y_pos;
}

Tile::~Tile()
{
	delete content;
}
