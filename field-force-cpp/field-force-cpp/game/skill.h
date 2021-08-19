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
	static std::vector<Skill> ALL_SKILLS;

	int id;
	std::string name;
	int cooldown;
	int range;
	int value;
	SkillType type;

	int cooldown_left = 0;

	Skill();
	Skill(int, int);
	Skill(int, std::string, int, int, int, SkillType);
};
