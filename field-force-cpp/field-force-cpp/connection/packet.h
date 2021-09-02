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

	int read_int();
	std::string read_string();
	void read_config();
	std::vector<Player> read_players();
	Skill read_skill();
	std::vector<std::vector<char>> read_map(int);
	std::vector<int> read_player_in_turn();
	std::vector<Fire> read_fires();
	std::vector<Wall> read_walls();
	std::vector<Consumable> read_consumables();
	Move* read_move();

	void write(std::vector<uint8_t>);
	void write(int);
	void write(std::string);
	void write(Move);
	void write(Skill);
	void write_length();

	static int reverse_int_byte_array(int);
	static int convert_byte_array_to_int(uint8_t*);
private:
	std::vector<Consumable> read_config_consumables();
	std::vector<Skill> read_config_skills();
	std::vector<uint8_t> int_to_byte_array(int);
	std::vector<uint8_t> to_byte_array();
};