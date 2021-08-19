#pragma once
#include <vector>

#include "tile.h"

class GameField
{
public:
	int dimension = 0;
	std::vector<std::vector<Tile>> field;
	std::vector<std::vector<char>> field_chars;

	GameField();
	GameField(int, std::vector<std::vector<char>>);

private:
	std::vector<std::vector<Tile>> createField(std::vector<std::vector<char>>);
};
