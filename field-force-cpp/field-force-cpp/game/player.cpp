#include "player.h"

Player::Player() { }

Player::Player(char _id, int _player_number, std::string _playername, int _x_pos, int _y_pos)
{
	id = _id;
	player_number = _player_number;
	playername = _playername;
	x_pos = _x_pos;
	y_pos = _y_pos;
}

Player::Player(char _id, int _player_number, std::string _playername, int _hp, int _shield, int _x_pos, int _y_pos, Skill _skill1, Skill _skill2)
{
	id = _id;
	player_number = _player_number;
	playername = _playername;
	hp = _hp;
	shield = _shield;
	x_pos = _x_pos;
	y_pos = _y_pos;
	skill1 = _skill1;
	skill2 = _skill2;
}
