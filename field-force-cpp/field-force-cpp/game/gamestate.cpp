#ifdef _WIN32
#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>
#ifdef _DEBUG
#ifndef DBG_NEW
#define DBG_NEW new ( _NORMAL_BLOCK , __FILE__ , __LINE__ )
#define new DBG_NEW
#endif
#endif  // _DEBUG
#endif
#include "gamestate.h"
#include "player.h"
#include "../board/fire.h"
#include "../board/wall.h"
#include "../board/consumable.h"

#include <typeinfo>

GameState::GameState() { }

GameState::GameState(GameField* gamefield, std::vector<int> _player_in_turn)
{
	current_field = gamefield;
	player_in_turn = _player_in_turn;
}

GameState::GameState(GameField* _gamefield, std::vector<Player> _players, std::vector<int> _player_in_turn, std::vector<Fire> _fires, std::vector<Wall> _walls, std::vector<Consumable> _consumables)
{
	current_field = _gamefield;
	current_players = _players;
	player_in_turn = _player_in_turn;
	fires = _fires;
	walls = _walls;
	consumables = _consumables;
	last_move = new Move();
}

std::vector<Move> GameState::getAllMoves(int player_id)
{
	std::vector<Move> moves;
	Player player = getPlayer(player_id);
	Tile* tile = current_field->field[player.x_pos][player.y_pos];
	for (int t = static_cast<int>(MoveType::MT_MOVEMENT); t != static_cast<int>(MoveType::MT_LAST); t++)
	{
		switch (MoveType(t))
		{
		case MoveType::MT_MOVEMENT:
			if (tile->nTile != nullptr)
				moves.emplace_back(Move(MoveType::MT_MOVEMENT, Direction::DIR_NORTH));
			if (tile->eTile != nullptr)
				moves.emplace_back(Move(MoveType::MT_MOVEMENT, Direction::DIR_EAST));
			if (tile->sTile != nullptr)
				moves.emplace_back(Move(MoveType::MT_MOVEMENT, Direction::DIR_SOUTH));
			if (tile->wTile != nullptr)
				moves.emplace_back(Move(MoveType::MT_MOVEMENT, Direction::DIR_WEST));
			break;
		case MoveType::MT_ATTACK:
			if (tile->nTile != nullptr && isValidTarget(tile->nTile->content->id))
				moves.emplace_back(Move(MoveType::MT_ATTACK, Direction::DIR_NORTH));
			if (tile->eTile != nullptr && isValidTarget(tile->eTile->content->id))
				moves.emplace_back(Move(MoveType::MT_ATTACK, Direction::DIR_EAST));
			if (tile->sTile != nullptr && isValidTarget(tile->sTile->content->id))
				moves.emplace_back(Move(MoveType::MT_ATTACK, Direction::DIR_SOUTH));
			if (tile->wTile != nullptr && isValidTarget(tile->wTile->content->id))
				moves.emplace_back(Move(MoveType::MT_ATTACK, Direction::DIR_WEST));
			break;
		case MoveType::MT_SKILL:
			for (int d = static_cast<int>(Direction::DIR_NORTH); d != static_cast<int>(Direction::DIR_LAST); d++)
			{
				if (player.skill1.cooldown_left == 0)
					moves.emplace_back(Move(MoveType::MT_SKILL, Direction(d), player.skill1));
				if (player.skill2.cooldown_left == 0)
					moves.emplace_back(Move(MoveType::MT_SKILL, Direction(d), player.skill2));
			}
			break;
		default:
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

void GameState::simulateNextGamestate(int player_id, Move* move)
{
	Player player = getPlayer(player_id);
	switch (move->type)
	{
	case MoveType::MT_MOVEMENT:
		if (move->direction == Direction::DIR_NORTH)
			moveToTile(&player, player.x_pos, player.y_pos - 1);
		if (move->direction == Direction::DIR_EAST)
			moveToTile(&player, player.x_pos + 1, player.y_pos);
		if (move->direction == Direction::DIR_SOUTH)
			moveToTile(&player, player.x_pos, player.y_pos + 1);
		if (move->direction == Direction::DIR_WEST)
			moveToTile(&player, player.x_pos - 1, player.y_pos);
		break;
	case MoveType::MT_ATTACK:
		if (move->direction == Direction::DIR_NORTH)
			attackTile(&player, player.x_pos, player.y_pos - 1);
		if (move->direction == Direction::DIR_EAST)
			attackTile(&player, player.x_pos + 1, player.y_pos);
		if (move->direction == Direction::DIR_SOUTH)
			attackTile(&player, player.x_pos, player.y_pos + 1);
		if (move->direction == Direction::DIR_WEST)
			attackTile(&player, player.x_pos - 1, player.y_pos);
		break;
	case MoveType::MT_SKILL:
		Skill* skill = &player.skill1;
		if (move->skill.id == player.skill2.id)
			skill = &player.skill2;
		skill->useSkill(&player, move->direction, this);
		break;
	}
}

void GameState::moveToTile(Player* player, int x_target, int y_target)
{

}

void GameState::attackTile(Player* player, int x_target, int y_target)
{
	MapObject* target = current_field->field[x_target][y_target]->content;
	if (typeid(target).name() == "Player")
	{
	}
	else
	{
		
	}

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

Player GameState::getPlayer(int id)
{
	for (auto p : current_players)
	{
		if (p.player_number == id)
			return p;
	}
}

GameState::~GameState()
{
	delete current_field;
	delete last_move;
}
