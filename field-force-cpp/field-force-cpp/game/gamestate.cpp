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

std::vector<Move> GameState::getAllMoves(int player_id)
{
	std::vector<Move> moves;
	Player* player = &current_players.at(player_id - 1);
	Tile* tile = &current_field.field[player->x_pos][player->y_pos];
	for (int t = MT_MOVEMENT; t != MT_LAST; t++)
	{
		switch (MoveType(t))
		{
		case MT_MOVEMENT:
			if (tile->nTile != nullptr)
				moves.push_back(Move(MT_MOVEMENT, DIR_NORTH));
			if (tile->eTile != nullptr)
				moves.push_back(Move(MT_MOVEMENT, DIR_EAST));
			if (tile->sTile != nullptr)
				moves.push_back(Move(MT_MOVEMENT, DIR_SOUTH));
			if (tile->wTile != nullptr)
				moves.push_back(Move(MT_MOVEMENT, DIR_WEST));
			break;
		case MT_ATTACK:
			if (tile->nTile != nullptr && isValidTarget(tile->nTile->content.id))
				moves.push_back(Move(MT_ATTACK, DIR_NORTH));
			if (tile->eTile != nullptr && isValidTarget(tile->eTile->content.id))
				moves.push_back(Move(MT_ATTACK, DIR_EAST));
			if (tile->sTile != nullptr && isValidTarget(tile->sTile->content.id))
				moves.push_back(Move(MT_ATTACK, DIR_SOUTH));
			if (tile->wTile != nullptr && isValidTarget(tile->wTile->content.id))
				moves.push_back(Move(MT_ATTACK, DIR_WEST));
			break;
		case MT_SKILL:
			for (int d = DIR_NORTH; d != DIR_LAST; d++)
			{
				if (player->skill1.cooldown_left == 0)
					moves.push_back(Move(MT_SKILL, Direction(d), player->skill1));
				if (player->skill2.cooldown_left == 0)
					moves.push_back(Move(MT_SKILL, Direction(d), player->skill2));
			}
			break;
		}
	}
	return moves;
}

bool GameState::isValidTarget(char c)
{
	for (char x : VALID_TARGETS)
	{
		if (x == c)
			return true;
	}
	return false;
}

void GameState::simulateNextGamestate(int player_id, Move move)
{

}

void GameState::moveToTile(Player player, int x_target, int y_target)
{

}

void GameState::attackTile(Player player, int x_target, int y_target)
{

}

void GameState::prepareForNextRound()
{
	for (auto p : current_players)
	{
		if (p.active)
			p.prepareForNextRound(this);
	}
	for (auto f : fires)
		f.prepareForNextRound(this);
}

bool GameState::isGameOver()
{
	return player_in_turn.size() <= 1;
}

Player GameState::getNextPlayer()
{
	for (auto p : current_players)
	{
		if (p.player_number == player_in_turn.at(0))
			return p;
	}
}

Player GameState::getOwnPlayer()
{
	for (auto p : current_players)
	{
		if (p.player_number == OWN_PLAYER_ID)
			return p;
	}
}