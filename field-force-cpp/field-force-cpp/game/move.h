#pragma once
#include "skill.h"

enum MoveType
{
	M_MOVEMENT = 1,
	ATTACK,
	SKILL,
};

enum Direction
{
	NORTH = 1,
	EAST,
	SOUTH,
	WEST,
};

class Move
{
public:
	MoveType type;
	Direction direction;
	Skill* skill;

	Move();
	Move(MoveType, Direction);
	Move(MoveType, Direction, Skill*);

};