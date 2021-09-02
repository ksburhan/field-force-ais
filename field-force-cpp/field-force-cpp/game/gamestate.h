#pragma once
#include "player.h"
#include "../board/gamefield.h"
#include "../board/fire.h"
#include "../board/wall.h"
#include "../board/consumable.h"
#include "../game/move.h"

class GameState
{
public:
	std::vector<int> player_in_turn;
	std::vector<Player> current_players;

	std::vector<Fire> fires;
	std::vector<Wall> walls;
	std::vector<Consumable> consumables;

	GameField* current_field;
	Move* last_move;

	GameState();
	GameState(GameField*, std::vector<int>);
	GameState(GameField*, std::vector<Player>, std::vector<int>, std::vector<Fire>, std::vector<Wall>, std::vector<Consumable>);

	std::vector<Move> get_all_moves(int);
	void simulate_next_gamestate(int, Move*);
	void move_to_tile(Player*, int, int);
	void attack_tile(Player*, int, int);
	void prepare_for_next_round();
	bool is_game_over();
	Player get_next_player();
	Player get_own_player();
	Player get_player(int);
	bool is_consumable(char);

	~GameState();
private:
	bool is_valid_target(char);
};