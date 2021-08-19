#include "mapobject.h"

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
