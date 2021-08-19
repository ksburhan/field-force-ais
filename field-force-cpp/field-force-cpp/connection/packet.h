#pragma once
#include <cstdint>
#include <string>
#include <vector>
#include "../game/move.h"
#include "../game/skill.h"
#include "../game/player.h"
#include "../board/consumable.h"
#include "../board/fire.h"
#include "../board/wall.h"

enum ServerPackets
{
	GAMEMODE = 2,
	PLAYERINFORMATION = 3,
	GAMEFIELD = 4,
	MOVEREQUEST = 5,
	NEWGAMESTATE = 7,
	MOVEDISTRIBUTION = 8,
	GAMEERROR = 9,
	GAMEOVER = 10
};

enum ClientPackets
{
	PLAYERNAME = 1,
	MOVEREPLY = 6,
};


class Packet
{
public:
	int read_pos = 0;
	std::vector<uint8_t> buffer;

	Packet();
	Packet(std::vector<uint8_t>);

	int readInt();
	std::string readString();
	void readConfig();
	std::vector<Player> readPlayers();
	Skill readSkill();
	std::vector<std::vector<char>> readMap(int);
	std::vector<int> readPlayerInTurn();
	std::vector<Fire> readFires();
	std::vector<Wall> readWalls();
	std::vector<Consumable> readConsumables();

	void write(std::vector<uint8_t>);
	void write(int);
	void write(float);
	void write(std::string);
	void write(Move);
	void writeLength();

	static int reverseIntByteArray(int);
	static int convertByteArrayToInt(uint8_t*);
private:
	std::vector<Consumable> readConfigConsumables();
	std::vector<Skill> readConfigSkills();
	uint8_t* intToByteArray(int);
	uint8_t* toByteArray();
};