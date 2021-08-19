#include "tile.h"

Tile::Tile() { }

Tile::Tile(int _x_pos, int _y_pos, MapObject* _content)
{
	x_pos = _x_pos;
	y_pos = _y_pos;
	content = _content;
}
