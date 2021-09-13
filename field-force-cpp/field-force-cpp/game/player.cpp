#ifdef _WIN32
#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>
#ifdef _DEBUG
#ifndef DBG_NEW
#define DBG_NEW new ( _NORMAL_BLOCK , __FILE__ , __LINE__ )
#define new DBG_NEW
#endif
#endif  // _DEBUG
#endif
#include "player.h"

#include "gameconstants.h"

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

/**
 * \brief if shield is available, shielddamage is taken first. if damage is still left reduce hp
 */
void Player::take_damage(int damage, GameState* game_state)
{
	if (shield > 0)
	{
		take_shield_damage(damage, game_state);
	}
	else
	{
		hp -= damage;
		if (hp <= 0)
			set_inactive(game_state);
	}
}

/**
 * \brief reduces shield damage, if damage is still left call takeDamage function with rest
 */
void Player::take_shield_damage(int shield_damage, GameState* game_state)
{
	shield -= shield_damage;
	if (shield <= 0)
	{
		int damage = shield * (-1);
		shield = 0;
		take_damage(damage, game_state);
	}
}

/**
 * \brief heal player by value
 */
void Player::heal(int heal)
{
	hp += heal;
	if (hp > HP)
		hp = HP;
}

/**
 * \brief charge shield by value
 */
void Player::charge_shield(int charge)
{
	shield += charge;
	if (shield > SHIELD)
		shield = SHIELD;
}

void Player::set_on_fire()
{
	on_fire = ON_FIRE_EFFECT_DURATION;
}

void Player::set_inactive(GameState* game_state)
{
	destroy(game_state);
	hp = 0;
	shield = 0;
	active = false;
}

void Player::prepare_for_next_round(GameState* game_state)
{
	if (on_fire > 0)
	{
		take_damage(ON_FIRE_DAMAGE, game_state);
		on_fire--;
	}
	skill1.prepare_for_next_round();
	skill2.prepare_for_next_round();
}

