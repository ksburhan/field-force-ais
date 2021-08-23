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
#include "move.h"

Move::Move() { }

Move::Move(MoveType _type, Direction _direction)
{
	type = _type;
	direction = _direction;
	skill = Skill();
}


Move::Move(MoveType _type, Direction _direction, Skill _skill)
{
	type = _type;
	direction = _direction;
	skill = _skill;
}

Move::~Move()
{

}
