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

void Fire::prepare_for_next_round(GameState* game_state)
{
	duration--;
	if (duration <= 0)
		destroy(game_state);
}
