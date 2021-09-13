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
#include "packet.h"
#include "../game/gameconstants.h"
#include "../game/skill.h"
#include "../board/consumable.h"
#include "../board/fire.h"
#include "../board/wall.h"

#include <iostream>
#include <algorithm>

Packet::Packet() { }

Packet::Packet(std::vector<uint8_t> data)
{
	buffer = data;
}

/**
 * \brief
 * reads 4 bytes and increased read pos
 */
int Packet::read_int()
{
	try
	{

		int value;
		std::memcpy(&value, &buffer[read_pos], sizeof(int));
		read_pos += 4;
		return value;
	}
	catch (...)
	{
		std::cerr << "Couldn't read value of type int!" << std::endl;
	}
}

/**
 * \brief reads 4 bytes for the length of the string.
 * reads the amount of bytes that are read in the length
 */
std::string Packet::read_string()
{
	try
	{
		int length = read_int();
		std::string msg;
		msg.resize(length + 1);
		for(int i = 0; i < length; i++)
		{
			msg[i] = buffer[read_pos + i];
		}
		msg[length] = '\0';
		read_pos += length;
		return msg;
	}
	catch (...)
	{
		std::cerr << "Couldn't read string!" << std::endl;
		return "";
	}
}

/**
 * \brief reads the full config needed to play the game
 */
void Packet::read_config()
{
	HP = read_int();
	SHIELD = read_int();

	ATTACK_DAMAGE = read_int();
	WALK_IN_PLAYER_DAMAGE = read_int();
	PLAYER_WALKED_INTO_DAMAGE = read_int();

	FIRE_DURATION_ON_MAP = read_int();
	ON_FIRE_EFFECT_DURATION = read_int();
	ON_FIRE_DAMAGE = read_int();

	WALL_HP = read_int();
	WALK_IN_WALL_DAMAGE = read_int();
	WALL_TAKE_DAMAGE = read_int();

	ALL_CONSUMABLES = read_config_consumables();

	ALL_SKILLS = read_config_skills();
}

/**
 * \brief reads all valid consumables and puts in global list VALID_CONSUMABLES
 */
std::vector<Consumable> Packet::read_config_consumables()
{
	std::vector<Consumable> cons;
	int con_count = read_int();
	for (int i = 0; i < con_count; i++)
	{
		char id = (char)read_int();
		std::string con_name = read_string();
		int healing = read_int();
		int shield = read_int();
		cons.push_back(Consumable(id, con_name, healing, shield));
	}
	return cons;
}

/**
 * \brief reads all valid skills
 */
std::vector<Skill> Packet::read_config_skills()
{
	std::vector<Skill> skills;
	int skills_count = read_int();
	for (int i = 0; i < skills_count; i++)
	{
		int id = read_int();
		std::string skill_name = read_string();
		int cooldown = read_int();
		int range = read_int();
		int value = read_int();
		int type = read_int();
		skills.push_back(Skill(id, skill_name, cooldown, range, value, static_cast<SkillType>(type)));
	}
	return skills;
}

/**
 * \brief reads playerinformation and returns a list of player objects
 */
std::vector<Player> Packet::read_players()
{
	std::vector<Player> players;
	int player_count = read_int();
	for (int i = 0; i < player_count; i++)
	{
		int player_number = read_int();
		std::string player_name = read_string();
		int hp = read_int();
		int shield = read_int();
		int x_pos = read_int();
		int y_pos = read_int();
		Skill skill1 = read_skill();
		Skill skill2 = read_skill();
		players.push_back(Player((char)player_number, player_number, player_name, hp, shield, x_pos, y_pos, skill1, skill2));
	}
	return players;
}

Skill Packet::read_skill()
{
	int skill_id = read_int();
	if(skill_id == -1)
	{
		return Skill();
	}
	int cooldown_left = read_int();
	return Skill(skill_id, cooldown_left);
}

/**
 * \brief reads all the chars for the map and puts in appropriate 2d array
 */
std::vector<std::vector<char>> Packet::read_map(int dimension)
{
	std::vector<std::vector<char>> map;
	map.resize(dimension);
	for (int i = 0; i < dimension; i++)
		map[i].resize(dimension);
	for (int y = 0; y < dimension; y++) 
		for (int x = 0; x < dimension; x++)
			map[x][y] = (char)read_int();
	return map;
}

/**
 * \brief reads the order in which players are supposed to play.
 * index 0 is next player to move
 */
std::vector<int> Packet::read_player_in_turn()
{
	std::vector<int> player_in_turn;
	int player_count = read_int();
	for(int i = 0; i < player_count; i++)
	{
		int player_number = read_int();
		player_in_turn.push_back(player_number);
	}
	return player_in_turn;
}

/**
 * \brief reads all the fires that are available on the map
 */
std::vector<Fire> Packet::read_fires()
{
	std::vector<Fire> fires;
	int fires_count = read_int();
	for (int i = 0; i < fires_count; i++)
	{
		int x = read_int();
		int y = read_int();
		int duration = read_int();
		fires.push_back(Fire('f', x, y, duration));
	}
	return fires;
}

/**
 * \brief reads all the walls that are available on the map
 */
std::vector<Wall> Packet::read_walls()
{
	std::vector<Wall> walls;
	int walls_count = read_int();
	for (int i = 0; i < walls_count; i++)
	{
		int x = read_int();
		int y = read_int();
		int hp = read_int();
		walls.push_back(Wall('-', x, y, hp));
	}
	return walls;
}

/**
 * \brief reads all the consumables that are available on the map
 */
std::vector<Consumable> Packet::read_consumables()
{
	std::vector<Consumable> consumables;
	int cons_count = read_int();
	for (int i = 0; i < cons_count; i++)
	{
		char id = (char)read_int();
		int x = read_int();
		int y = read_int();
		consumables.push_back(Consumable(id, x, y));
	}
	return consumables;
}

/**
 * \brief reads a move object
 */
Move* Packet::read_move()
{
	MoveType type = (MoveType)read_int();
	Direction dir = (Direction)read_int();
	Skill skill = read_skill();
	return new Move(type, dir, skill);
}


/**
 * \brief writes bytes in byte array
 */
void Packet::write(std::vector<uint8_t> value)
{
	for (int i = 0; i < sizeof(value); i++)
	{
		buffer.push_back(value[i]);
	}
}

/**
 * \brief writes int value as bytes in byte array
 */
void Packet::write(int value)
{
	std::vector<uint8_t> data = int_to_byte_array(value);
	for (int i = 0; i < data.size()-1; i++)
	{
		buffer.push_back(data[i]);
	}
}

/**
 * \brief writes string as bytes in byte array
 */
void Packet::write(std::string value)
{
	write((int)value.length());
	for (uint8_t b : value)
	{
		buffer.push_back(b);
	}
}

/**
 * \brief writes move object as bytes in byte array
 */
void Packet::write(Move move)
{
	write(static_cast<int>(move.type));
	write(static_cast<int>(move.direction));
	write(move.skill);
}

/**
 * \brief writes skill object as bytes in byte array
 */
void Packet::write(Skill skill)
{
	write(skill.id);
}

/**
 * \brief prepends the length of current data in first 4 indexes of byte array
 */
void Packet::write_length()
{
	std::vector<uint8_t> length = int_to_byte_array(buffer.size());
	for (int i = 0; i < length.size()-1; i++)
	{
		buffer.insert(buffer.begin() + i, length[i]);
	}
}

std::vector<uint8_t> Packet::int_to_byte_array(int value)
{
	value = reverse_int_byte_array(value);
	std::vector<uint8_t> ret;
	ret.resize(5);
	for (int i = 0; i < 4; i++)
		ret[3 - i] = (value >> (i * 8));
	return ret;
}

std::vector<uint8_t> Packet::to_byte_array()
{
	int n = buffer.size();
	std::vector<uint8_t> ret;
	ret.resize(n);
	for (int i = 0; i < n; i++)
	{
		ret[i] = buffer.at(i);
	}
	return ret;
}

int Packet::reverse_int_byte_array(int data)
{
	return ((data >> 24) & 0xff) |
		((data << 8) & 0xff0000) | 
		((data >> 8) & 0xff00) | 
		((data << 24) & 0xff000000); 
}

int Packet::convert_byte_array_to_int(uint8_t* data)
{
	return int((unsigned char)(data[3]) << 24 |
		(unsigned char)(data[2]) << 16 |
		(unsigned char)(data[1]) << 8 |
		(unsigned char)(data[0]));
}