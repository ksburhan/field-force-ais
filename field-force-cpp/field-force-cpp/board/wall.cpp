#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>
#ifdef _DEBUG
#ifndef DBG_NEW
#define DBG_NEW new ( _NORMAL_BLOCK , __FILE__ , __LINE__ )
#define new DBG_NEW
#endif
#endif  // _DEBUG
#include "wall.h"

Wall::Wall() { }

Wall::Wall(char _id, int _x_pos, int _y_pos)
{
	id = _id;
	x_pos = _x_pos;
	y_pos = _y_pos;
}

Wall::Wall(char _id, int _x_pos, int _y_pos, int _hp)
{
	id = _id;
	x_pos = _x_pos;
	y_pos = _y_pos;
	hp = _hp;
}

void Wall::takeDamage(int damage, GameState* game_state)
{
	hp -= damage;
	if (hp <= 0)
		destroy(game_state);
}
