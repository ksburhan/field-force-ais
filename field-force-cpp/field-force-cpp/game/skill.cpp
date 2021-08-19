#include "skill.h"

Skill::Skill() { }

Skill::Skill(int _id, int _cooldown_left)
{
	id = _id;
	cooldown_left = _cooldown_left;
	Skill s = Skill::ALL_SKILLS.at(id);
	name = s.name;
	cooldown = s.cooldown;
	range = s.range;
	value = s.value;
	type = s.type;
}

Skill::Skill(int _id, std::string _name, int _cooldown, int _range, int _value, SkillType _type)
{
	id = _id;
	name = _name;
	cooldown = _cooldown;
	range = _range;
	value = _value;
	type = _type;
}
