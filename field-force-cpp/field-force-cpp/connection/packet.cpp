#include "packet.h"
#include "../game/gameconstants.h"
#include "../game/skill.h"
#include "../board/consumable.h"

#include <iostream>
#include <algorithm>

Packet::Packet() { }

Packet::Packet(uint8_t* data)
{
	write(data);
	readbuf = toByteArray();
}

int Packet::readInt()
{
	try
	{

		int value;
		std::memcpy(&value, readbuf + read_pos, sizeof(int));
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
		std::string message;
		std::memcpy(&message, readbuf + read_pos, length);
		if (message.length() > 0)
		{
			read_pos += length;
		}
		return message;
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

	Consumable::ALL_CONSUMABLES = readConfigConsumables();

	Skill::ALL_SKILLS = readConfigSkills();
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

void Packet::write(uint8_t* value)
{
	for (int i = 0; i < sizeof(value)/sizeof(uint8_t); i++)
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