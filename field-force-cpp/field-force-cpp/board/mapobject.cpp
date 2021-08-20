#include "mapobject.h"

#include "../game/gamestate.h"

MapObject::MapObject() { }

MapObject::MapObject(char _id, int _x_pos, int _y_pos)
{
	id = _id;
	x_pos = _x_pos;
	y_pos = _y_pos;
}

void MapObject::setPos(int _x_pos, int _y_pos)
{
	x_pos = _x_pos;
	y_pos = _y_pos;
}

void MapObject::destroy(GameState* game_state)
{
	game_state->current_field.field_chars[x_pos][y_pos] = '0';
	game_state->current_field.field[x_pos][y_pos].content = MapObject('0', x_pos, y_pos);
}