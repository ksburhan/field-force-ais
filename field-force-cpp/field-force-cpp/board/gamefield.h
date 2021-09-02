#pragma once
#include <vector>

#include "tile.h"

class Player;

class GameField
{
public:
	int dimension = 0;
	std::vector<std::vector<Tile*>> field;
	std::vector<std::vector<char>> field_chars;

	GameField();
	GameField(int, std::vector<std::vector<char>>, std::vector<Player>*);

	void print_map();

	~GameField();
private:
	std::vector<std::vector<Tile*>> createField(std::vector<std::vector<char>>, const std::vector<Player>&);
	Player get_player(int,const std::vector<Player>&);
};
