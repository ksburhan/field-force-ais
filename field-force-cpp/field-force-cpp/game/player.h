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

	void take_damage(int, GameState*);
	void take_shield_damage(int, GameState*);
	void heal(int);
	void charge_shield(int);
	void set_on_fire();
	void set_inactive(GameState*);
	void prepare_for_next_round(GameState*);
};

inline std::vector<Player> ALL_PLAYERS;