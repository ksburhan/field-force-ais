#pragma once
#include <string>
#include <vector>

enum SkillType
{
	MOVEMENT = 0,
	REGENERATE,
	FIRE,
	ROCKET,
	PUSH,
	BREAK
};

class Skill
{
public:

	int id = -1;
	std::string name;
	int cooldown = -1;
	int range = -1;
	int value = -1;
	SkillType type;

	int cooldown_left = 0;

	Skill();
	Skill(int, int);
	Skill(int, std::string, int, int, int, SkillType);

	void setOnCooldown();
	void prepareForNextRound();
};

inline std::vector<Skill> ALL_SKILLS;