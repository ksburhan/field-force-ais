#pragma once
#include <string>

#include "skill.h"
#include "../board/mapobject.h"

class Player: public MapObject
{
public:

	int player_number;
	std::string playername;
	int hp;
	int shield;

	bool active = true;
	int on_fire = 0;

	Skill skill1;
	Skill skill2;

	Player();
	Player(char, int, std::string, int, int);
	Player(char, int, std::string, int, int, int, int, Skill, Skill);
};

inline std::vector<Player> ALL_PLAYERS;