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
	//write(data);
}

int Packet::readInt()
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

std::string Packet::readString()
{
	try
	{
		int length = readInt();
		char* msg = new char[length+1];
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
	}
}

void Packet::readConfig()
{
	HP = readInt();
	SHIELD = readInt();

	ATTACK_DAMAGE = readInt();
	WALK_IN_PLAYER_DAMAGE = readInt();
	PLAYER_WALKED_INTO_DAMAGE = readInt();

	FIRE_DURATION_ON_MAP = readInt();
	ON_FIRE_EFFECT_DURATION = readInt();
	ON_FIRE_DAMAGE = readInt();

	WALL_HP = readInt();
	WALK_IN_WALL_DAMAGE = readInt();
	WALL_TAKE_DAMAGE = readInt();

	ALL_CONSUMABLES = readConfigConsumables();

	ALL_SKILLS = readConfigSkills();
}

std::vector<Consumable> Packet::readConfigConsumables()
{
	std::vector<Consumable> cons;
	int con_count = readInt();
	for (int i = 0; i < con_count; i++)
	{
		char id = (char)readInt();
		std::string con_name = readString();
		int healing = readInt();
		int shield = readInt();
		cons.push_back(Consumable(id, con_name, healing, shield));
	}
	return cons;
}

std::vector<Skill> Packet::readConfigSkills()
{
	std::vector<Skill> skills;
	int skills_count = readInt();
	for (int i = 0; i < skills_count; i++)
	{
		int id = readInt();
		std::string skill_name = readString();
		int cooldown = readInt();
		int range = readInt();
		int value = readInt();
		int type = readInt();
		skills.push_back(Skill(id, skill_name, cooldown, range, value, static_cast<SkillType>(type)));
	}
	return skills;
}

std::vector<Player> Packet::readPlayers()
{
	std::vector<Player> players;
	int player_count = readInt();
	for (int i = 0; i < player_count; i++)
	{
		int player_number = readInt();
		std::string player_name = readString();
		int hp = readInt();
		int shield = readInt();
		int x_pos = readInt();
		int y_pos = readInt();
		Skill skill1 = readSkill();
		Skill skill2 = readSkill();
		players.push_back(Player((char)player_number, player_number, player_name, hp, shield, x_pos, y_pos, skill1, skill2));
	}
	return players;
}

Skill Packet::readSkill()
{
	int skill_id = readInt();
	if(skill_id == -1)
	{
		return Skill();
	}
	int cooldown_left = readInt();
	return Skill(skill_id, cooldown_left);
}

std::vector<std::vector<char>> Packet::readMap(int dimension)
{
	std::vector<std::vector<char>> map;
	map.resize(dimension);
	for (int i = 0; i < dimension; i++)
		map[i].resize(dimension);
	for (int y = 0; y < dimension; y++) 
		for (int x = 0; x < dimension; x++)
			map[x][y] = (char)readInt();
	return map;
}

std::vector<int> Packet::readPlayerInTurn()
{
	std::vector<int> player_in_turn;
	int player_count = readInt();
	player_in_turn.resize(player_count);
	for(int i = 0; i < player_count; i++)
	{
		int player_number = readInt();
		player_in_turn.push_back(player_number);
	}
	return player_in_turn;
}

std::vector<Fire> Packet::readFires()
{
	std::vector<Fire> fires;
	int fires_count = readInt();
	fires.resize(fires_count);
	for (int i = 0; i < fires_count; i++)
	{
		int x = readInt();
		int y = readInt();
		int duration = readInt();
		fires.push_back(Fire('f', x, y, duration));
	}
	return fires;
}

std::vector<Wall> Packet::readWalls()
{
	std::vector<Wall> walls;
	int walls_count = readInt();
	walls.resize(walls_count);
	for (int i = 0; i < walls_count; i++)
	{
		int x = readInt();
		int y = readInt();
		int hp = readInt();
		walls.push_back(Wall('-', x, y, hp));
	}
	return walls;
}

std::vector<Consumable> Packet::readConsumables()
{
	std::vector<Consumable> consumables;
	int cons_count = readInt();
	consumables.resize(cons_count);
	for (int i = 0; i < cons_count; i++)
	{
		char id = (char)readInt();
		int x = readInt();
		int y = readInt();
		consumables.push_back(Consumable(id, x, y));
	}
	return consumables;
}

void Packet::write(std::vector<uint8_t> value)
{
	for (int i = 0; i < sizeof(value); i++)
	{
		buffer.push_back(value[i]);
	}
}

void Packet::write(int value)
{
	uint8_t* data = intToByteArray(value);
	for (int i = 0; i < sizeof(data) / sizeof(uint8_t); i++)
	{
		buffer.push_back(data[i]);
	}
}

void Packet::write(std::string value)
{
	write((int)value.length());
	for (uint8_t b : value)
	{
		buffer.push_back(b);
	}
}

void Packet::write(Move)
{
	write(1);
}


void Packet::writeLength()
{
	uint8_t* length = intToByteArray(buffer.size());
	for (int i = 0; i < sizeof(length) / sizeof(uint8_t); i++)
	{
		buffer.insert(buffer.begin() + i, length[i]);
	}
}

uint8_t* Packet::intToByteArray(int value)
{
	value = reverseIntByteArray(value);
	uint8_t* ret = new uint8_t[5];
	for (int i = 0; i < 4; i++)
		ret[3 - i] = (value >> (i * 8));
	return ret;
}

uint8_t* Packet::toByteArray()
{
	int n = buffer.size();
	uint8_t* ret = new uint8_t[n];
	for (int i = 0; i < n; i++)
	{
		ret[i] = buffer.at(i);
	}
	return ret;
}

int Packet::reverseIntByteArray(int data)
{
	return ((data >> 24) & 0xff) |
		((data << 8) & 0xff0000) | 
		((data >> 8) & 0xff00) | 
		((data << 24) & 0xff000000); 
}

int Packet::convertByteArrayToInt(uint8_t* data)
{
	return int((unsigned char)(data[3]) << 24 |
		(unsigned char)(data[2]) << 16 |
		(unsigned char)(data[1]) << 8 |
		(unsigned char)(data[0]));
}