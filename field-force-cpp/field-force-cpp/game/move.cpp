#include "move.h"

Move::Move() { }

Move::Move(MoveType _type, Direction _direction)
{
	type = _type;
	direction = _direction;
}


Move::Move(MoveType _type, Direction _direction, Skill _skill)
{
	type = _type;
	direction = _direction;
	skill = _skill;
}
