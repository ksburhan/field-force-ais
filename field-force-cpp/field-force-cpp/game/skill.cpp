#include "skill.h"

Skill::Skill() { } 

Skill::Skill(int _id, std::string _name, int _cooldown, int _range, int _value, SkillType _type)
{
	id = _id;
	name = _name;
	cooldown = _cooldown;
	range = _range;
	value = _value;
	type = _type;
}
