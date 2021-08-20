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

void Player::takeDamage(int damage, GameState* game_state)
{
	if (shield > 0)
	{
		takeShieldDamage(damage, game_state);
	}
	else
	{
		hp -= damage;
		if (hp <= 0)
			setInactive(game_state);
	}
}

void Player::takeShieldDamage(int shield_damage, GameState* game_state)
{
	shield -= shield_damage;
	if (shield <= 0)
	{
		int damage = shield * (-1);
		shield = 0;
		takeDamage(damage, game_state);
	}
}

void Player::heal(int heal)
{
	hp += heal;
	if (hp > HP)
		hp = HP;
}

void Player::chargeShield(int charge)
{
	shield += charge;
	if (shield > SHIELD)
		shield = SHIELD;
}

void Player::setOnFire()
{
	on_fire = ON_FIRE_EFFECT_DURATION;
}

void Player::setInactive(GameState* game_state)
{
	destroy(game_state);
	hp = 0;
	shield = 0;
	active = false;
}

void Player::prepareForNextRound(GameState* game_state)
{
	if (on_fire > 0)
	{
		takeDamage(ON_FIRE_DAMAGE, game_state);
		on_fire--;
	}
	skill1.prepareForNextRound();
	skill2.prepareForNextRound();
}

