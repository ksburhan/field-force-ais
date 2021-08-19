#pragma once
#include "player.h"
#include "../board/gamefield.h"
#include "../board/fire.h"
#include "../board/wall.h"
#include "../board/consumable.h"
#include "../game//move.h"

class GameState
{
public:
	std::vector<int> player_in_turn;
	std::vector<Player> current_players;

	std::vector<Fire> fires;
	std::vector<Wall> walls;
	std::vector<Consumable> consumables;

	GameField current_field;
	Move last_move;

	GameState();
	GameState(GameField, std::vector<int>);
	GameState(GameField, std::vector<Player>, std::vector<int>, std::vector<Fire>, std::vector<Wall>, std::vector<Consumable>);
};