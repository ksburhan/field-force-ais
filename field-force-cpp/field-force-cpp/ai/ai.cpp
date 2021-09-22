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
#include "ai.h"

#include <ctime>

AI& AI::get_instance()
{
	static AI instance;

	return instance;
}

/**
 * \brief function the players should be changing to return the move their AI picks
 * \return returns a random valid move
 */
Move AI::get_best_move()
{
	std::vector<Move> moves = current_gamestate->get_all_moves(OWN_PLAYER_ID);
	Player op = current_gamestate->get_own_player();
	Move move = moves.at(std::rand() % moves.size() + 0);
	enemy_distance(current_gamestate);
	consum_distance(current_gamestate);
	if (enemy_distance(current_gamestate) < 6)
	{
		for (auto m : moves)
			if (m.type == MoveType::MT_ATTACK)
				return m;
		if (op.skill1.cooldown_left == 0 || op.skill2.cooldown_left == 0)
		{
			for (auto m : moves)
				if (m.type == MoveType::MT_SKILL && m.direction == enemy_direction)
					return m;
		}
		for (auto m : moves)
			if (m.type == MoveType::MT_MOVEMENT && m.direction == enemy_direction)
				return m;
	}
	else
	{
		if (consum_distance(current_gamestate) < 4)
		{
			if(op.hp < HP || op.shield < SHIELD)
			{
				for (auto m : moves)
					if (m.type == MoveType::MT_MOVEMENT && m.direction == consum_direction)
						return m;
			}
			else
			{
				for (auto m : moves)
					if (m.type == MoveType::MT_MOVEMENT && m.direction == enemy_direction)
						return m;
			}
		}
		else
		{
			for (auto m : moves)
				if (m.type == MoveType::MT_MOVEMENT && m.direction == enemy_direction)
					return m;
		}
	}

	/*if (consum_distance(current_gamestate) < 4)
	{
		if (op.hp < HP || op.shield < SHIELD)
		{
			for (auto m : moves)
				if (m.type == MoveType::MT_MOVEMENT && m.direction == consum_direction)
					return m;
		}
		else
		{
			for (auto m : moves)
				if (m.type == MoveType::MT_MOVEMENT && m.direction == enemy_direction)
					return m;
		}
	}
	else
	{
		if (enemy_distance(current_gamestate) < 6)
		{
			for (auto m : moves)
				if (m.type == MoveType::MT_ATTACK)
					return m;
			if (op.skill1.cooldown_left == 0 || op.skill2.cooldown_left == 0)
			{
				for (auto m : moves)
					if (m.type == MoveType::MT_SKILL && m.direction == enemy_direction)
						return m;
			}
			for (auto m : moves)
				if (m.type == MoveType::MT_MOVEMENT && m.direction == enemy_direction)
					return m;
		}
		else
		{
			for (auto m : moves)
				if (m.type == MoveType::MT_MOVEMENT && m.direction == enemy_direction)
					return m;
		}
	}*/
	return move;
}

int AI::enemy_distance(GameState* game_state)
{
	Player player = game_state->get_own_player();
	int d = 100;
	for (Player p : game_state->current_players)
	{
		if (p.player_number != player.player_number)
		{
			int dx = p.x_pos - player.x_pos;
			int dy = p.y_pos - player.y_pos;
			if (dy < 0 && abs(dx) <= abs(dy))
				enemy_direction = Direction::DIR_NORTH;
			if (dx > 0 && abs(dx) >= abs(dy))
				enemy_direction = Direction::DIR_EAST;
			if (dy > 0 && abs(dx) <= abs(dy))
				enemy_direction = Direction::DIR_SOUTH;
			if (dx < 0 && abs(dx) >= abs(dy))
				enemy_direction = Direction::DIR_WEST;
			if (abs(dx) + abs(dy) < d)
				d = dx + dy;
		}
	}
	return d;
}

int AI::consum_distance(GameState* game_state)
{
	Player player = game_state->get_own_player();
	int d = 100;
	for (Consumable c : game_state->consumables)
	{
		int dx = c.x_pos - player.x_pos;
		int dy = c.y_pos - player.y_pos;
		if (dy < 0 && abs(dx) <= abs(dy))
			consum_direction = Direction::DIR_NORTH;
		if (dx > 0 && abs(dx) >= abs(dy))
			consum_direction = Direction::DIR_EAST;
		if (dy > 0 && abs(dx) <= abs(dy))
			consum_direction = Direction::DIR_SOUTH;
		if (dx > 0 && abs(dx) >= abs(dy))
			consum_direction = Direction::DIR_WEST;
		if (abs(dx) + abs(dy) < d)
			d = dx + dy;
	}
	return d;
}

