#pragma once
#include <string>
#include <vector>


enum class Direction;
class GameState;
class Player;

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

	void set_on_cooldown();
	void prepare_for_next_round();
	void use_skill(Player*, Direction, GameState*);

	~Skill();
private:
	void movement_type(Player*, Direction, GameState*);
	void regenerate_type(Player*, Direction, GameState*);
	void fire_type(Player*, Direction, GameState*);
	void rocket_type(Player*, Direction, GameState*);
	void push_type(Player*, Direction, GameState*);
	void break_type(Player*, Direction, GameState*);
};

inline std::vector<Skill> ALL_SKILLS;