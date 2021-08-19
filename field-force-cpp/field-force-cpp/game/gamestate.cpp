#include "gamestate.h"
#include "player.h"
#include "../board/fire.h"
#include "../board/wall.h"
#include "../board/consumable.h"

GameState::GameState() { }

GameState::GameState(GameField gamefield, std::vector<int> _player_in_turn)
{
	current_field = gamefield;
	player_in_turn = _player_in_turn;
}

GameState::GameState(GameField _gamefield, std::vector<Player> _players, std::vector<int> _player_in_turn, std::vector<Fire> _fires, std::vector<Wall> _walls, std::vector<Consumable> _consumables)
{
	current_field = _gamefield;
	current_players = _players;
	player_in_turn = _player_in_turn;
	fires = _fires;
	walls = _walls;
	consumables = _consumables;
}
