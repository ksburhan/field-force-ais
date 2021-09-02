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

#include <iostream>

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

std::vector<Move> GameState::get_all_moves(int player_id)
{
	std::vector<Move> moves;
	Player player = get_player(player_id);
	Tile* tile = current_field->field[player.x_pos][player.y_pos];
	for (int t = static_cast<int>(MoveType::MT_MOVEMENT); t != static_cast<int>(MoveType::MT_LAST); t++)
	{
		switch (MoveType(t))
		{
		case MoveType::MT_MOVEMENT:
			if (tile->n_tile != nullptr)
				moves.emplace_back(Move(MoveType::MT_MOVEMENT, Direction::DIR_NORTH));
			if (tile->e_tile != nullptr)
				moves.emplace_back(Move(MoveType::MT_MOVEMENT, Direction::DIR_EAST));
			if (tile->s_tile != nullptr)
				moves.emplace_back(Move(MoveType::MT_MOVEMENT, Direction::DIR_SOUTH));
			if (tile->w_tile != nullptr)
				moves.emplace_back(Move(MoveType::MT_MOVEMENT, Direction::DIR_WEST));
			break;
		case MoveType::MT_ATTACK:
			if (tile->n_tile != nullptr && is_valid_target(tile->n_tile->content->id))
				moves.emplace_back(Move(MoveType::MT_ATTACK, Direction::DIR_NORTH));
			if (tile->e_tile != nullptr && is_valid_target(tile->e_tile->content->id))
				moves.emplace_back(Move(MoveType::MT_ATTACK, Direction::DIR_EAST));
			if (tile->s_tile != nullptr && is_valid_target(tile->s_tile->content->id))
				moves.emplace_back(Move(MoveType::MT_ATTACK, Direction::DIR_SOUTH));
			if (tile->w_tile != nullptr && is_valid_target(tile->w_tile->content->id))
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

bool GameState::is_valid_target(char c)
{
	for (char x : VALID_TARGETS)
	{
		if (x == c)
			return true;
	}
	return false;
}

void GameState::simulate_next_gamestate(int player_id, Move* move)
{
	Player playerC = get_player(player_id);
	Player* player = static_cast<Player*>(current_field->field[playerC.x_pos][playerC.y_pos]->content);
	switch (move->type)
	{
	case MoveType::MT_MOVEMENT:
		if (move->direction == Direction::DIR_NORTH)
			move_to_tile(player, player->x_pos, player->y_pos - 1);
		if (move->direction == Direction::DIR_EAST)
			move_to_tile(player, player->x_pos + 1, player->y_pos);
		if (move->direction == Direction::DIR_SOUTH)
			move_to_tile(player, player->x_pos, player->y_pos + 1);
		if (move->direction == Direction::DIR_WEST)
			move_to_tile(player, player->x_pos - 1, player->y_pos);
		break;
	case MoveType::MT_ATTACK:
		if (move->direction == Direction::DIR_NORTH)
			attack_tile(player, player->x_pos, player->y_pos - 1);
		if (move->direction == Direction::DIR_EAST)
			attack_tile(player, player->x_pos + 1, player->y_pos);
		if (move->direction == Direction::DIR_SOUTH)
			attack_tile(player, player->x_pos, player->y_pos + 1);
		if (move->direction == Direction::DIR_WEST)
			attack_tile(player, player->x_pos - 1, player->y_pos);
		break;
	case MoveType::MT_SKILL:
		Skill* skill = &player->skill1;
		if (move->skill.id == player->skill2.id)
			skill = &player->skill2;
		skill->use_skill(player, move->direction, this);
		break;
	}
}

void GameState::move_to_tile(Player* player, int x_target, int y_target)
{
	MapObject* target = current_field->field[x_target][y_target]->content;
	char target_char = current_field->field_chars[x_target][y_target];
	int x = player->x_pos;
	int y = player->y_pos;
	if(target_char == '0')
	{
		delete current_field->field[x_target][y_target]->content;
		current_field->field_chars[x_target][y_target] = player->id;
		current_field->field[x_target][y_target]->content = player;
		current_field->field_chars[x][y] = '0';
		current_field->field[x][y]->content = new MapObject('0', x, y);
		player->set_pos(x_target, y_target);
	}
	else if (target_char == '1' || target_char == '2' || target_char == '3' || target_char == '4')
	{
		player->take_damage(WALK_IN_PLAYER_DAMAGE, this);
		static_cast<Player*>(target)->take_damage(PLAYER_WALKED_INTO_DAMAGE, this);
	}
	else if (target_char == 'f')
	{
		delete current_field->field[x_target][y_target]->content;
		current_field->field_chars[x_target][y_target] = player->id;
		current_field->field[x_target][y_target]->content = player;
		current_field->field_chars[x][y] = '0';
		current_field->field[x][y]->content = new MapObject('0', x, y);
		player->set_pos(x_target, y_target);
		player->take_damage(ON_FIRE_DAMAGE, this);
		player->set_on_fire();
	}
	else if (target_char == '-')
	{
		player->take_damage(WALK_IN_WALL_DAMAGE, this);
		static_cast<Wall*>(target)->take_damage(ATTACK_DAMAGE, this);
	}
	else if (is_consumable(target_char))
	{
		Consumable* con = static_cast<Consumable*>(target);
		if (con->healing > 0)
			player->heal(con->healing);
		else
			player->take_damage(con->healing, this);
		if (con->shield > 0)
			player->charge_shield(con->shield);
		delete current_field->field[x_target][y_target]->content;
		current_field->field_chars[x_target][y_target] = player->id;
		current_field->field[x_target][y_target]->content = player;
		current_field->field_chars[x][y] = '0';
		current_field->field[x][y]->content = new MapObject('0', x, y);
		player->set_pos(x_target, y_target);
	}
}

void GameState::attack_tile(Player* player, int x_target, int y_target)
{
	MapObject* target = current_field->field[x_target][y_target]->content;
	char target_char = current_field->field_chars[x_target][y_target];
	if (target_char == '1' || target_char == '2' || target_char == '3' || target_char == '4')
	{
		static_cast<Player*>(target)->take_damage(ATTACK_DAMAGE, this);
	}
	else if(target_char == '-')
	{
		static_cast<Wall*>(target)->take_damage(ATTACK_DAMAGE, this);
	}
}

bool GameState::is_consumable(char c)
{
	for (auto con : ALL_CONSUMABLES)
	{
		if (con.id == c)
			return true;
	}
	return false;
}

void GameState::prepare_for_next_round()
{
	for (auto p : current_players)
	{
		if (p.active)
			p.prepare_for_next_round(this);
	}
	for (auto f : fires)
		f.prepare_for_next_round(this);
}

bool GameState::is_game_over()
{
	return player_in_turn.size() <= 1;
}

Player GameState::get_next_player()
{
	for (auto p : current_players)
	{
		if (p.player_number == player_in_turn.at(0))
			return p;
	}
}

Player GameState::get_own_player()
{
	for (auto p : current_players)
	{
		if (p.player_number == OWN_PLAYER_ID)
			return p;
	}
}

Player GameState::get_player(int id)
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
