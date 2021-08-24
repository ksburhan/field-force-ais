#pragma once
#include "skill.h"

enum class MoveType
{
	MT_MOVEMENT = 1,
	MT_ATTACK,
	MT_SKILL,
	MT_LAST,
};

enum class Direction
{
	DIR_NORTH = 1,
	DIR_EAST,
	DIR_SOUTH,
	DIR_WEST,
	DIR_LAST,
};

class Move
{
public:
	MoveType type;
	Direction direction;
	Skill skill;

	Move();
	Move(MoveType, Direction);
	Move(MoveType, Direction, Skill);

	~Move();
};