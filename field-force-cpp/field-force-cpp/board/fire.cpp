#include "fire.h"

Fire::Fire() { }

Fire::Fire(char _id, int _x_pos, int _y_pos)
{
	id = _id;
	x_pos = _x_pos;
	y_pos = _y_pos;
}

Fire::Fire(char _id, int _x_pos, int _y_pos, int _duration)
{
	id = _id;
	x_pos = _x_pos;
	y_pos = _y_pos;
	duration = _duration;
}
