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
#include "skill.h"

#include "move.h"
#include "gamestate.h"
#include "../board/mapobject.h"
#include "../board/tile.h"

Skill::Skill() { }

Skill::Skill(int _id, int _cooldown_left)
{
	id = _id;
	cooldown_left = _cooldown_left;
	Skill s = ALL_SKILLS.at(id);
	name = s.name;
	cooldown = s.cooldown;
	range = s.range;
	value = s.value;
	type = s.type;
}

Skill::Skill(int _id, std::string _name, int _cooldown, int _range, int _value, SkillType _type)
{
	id = _id;
	name = _name;
	cooldown = _cooldown;
	range = _range;
	value = _value;
	type = _type;
}

void Skill::set_on_cooldown()
{
	cooldown_left = cooldown;
}

void Skill::prepare_for_next_round()
{
	if(cooldown_left > 0)
		cooldown_left--;
}

/**
 * \brief calls function according to what type skill is and changes the gamestate accordingly.
 * When creating new skilltypes add here and corresponding function below
 */
void Skill::use_skill(Player* player, Direction direction, GameState* game_state)
{
    switch (type)
    {
    case MOVEMENT:
        movement_type(player, direction, game_state);
        break;
    case REGENERATE:
        regenerate_type(player, direction, game_state);
        break;
    case FIRE:
        fire_type(player, direction, game_state);
        break;
    case ROCKET:
        rocket_type(player, direction, game_state);
        break;
    case PUSH:
        push_type(player, direction, game_state);
        break;
    case BREAK:
        break_type(player, direction, game_state);
        break;
    }
}

/**
 * \brief moves player in chosen direction for amount of range the skill has
 */
void Skill::movement_type(Player* player, Direction direction, GameState* game_state)
{
    for (int i = 0; i < range; i++)
    {
        Tile* tile = game_state->current_field->field[player->x_pos][player->y_pos];
        if (direction == Direction::DIR_NORTH)
        {
            if (tile->n_tile != nullptr)
                game_state->move_to_tile(player, player->x_pos, player->y_pos - 1);
            else
                return;
        }
        if (direction == Direction::DIR_EAST)
        {
            if (tile->e_tile != nullptr)
                game_state->move_to_tile(player, player->x_pos + 1, player->y_pos);
            else
                return;
        }
        if (direction == Direction::DIR_SOUTH)
        {
            if (tile->s_tile != nullptr)
                game_state->move_to_tile(player, player->x_pos, player->y_pos + 1);
            else
                return;
        }
        if (direction == Direction::DIR_WEST)
        {
            if (tile->w_tile != nullptr)
                game_state->move_to_tile(player, player->x_pos - 1, player->y_pos);
            else
                return;
        }
    }
}


/**
 * \brief heals player by value of skill
 */
void Skill::regenerate_type(Player* player, Direction direction, GameState* game_state)
{
    player->heal(value);
}

/**
 * \brief adds fires amount of range of skill in direction player chose
 * stops when hitting a wall
 */
void Skill::fire_type(Player* player, Direction direction, GameState* game_state)
{
    int x_target = player->x_pos;
    int y_target = player->y_pos;
    Tile* tile;
    for (int i = 0; i < range; i++)
    {
        tile = game_state->current_field->field[x_target][y_target];
        if (direction == Direction::DIR_NORTH)
        {
            if (tile->n_tile != nullptr)
                y_target -= 1;
            else
                return;
        }
        if (direction == Direction::DIR_EAST)
        {
            if (tile->e_tile != nullptr)
                x_target += 1;
            else
                return;
        }
        if (direction == Direction::DIR_SOUTH)
        {
            if (tile->s_tile != nullptr)
                y_target += 1;
            else
                return;
        }
        if (direction == Direction::DIR_WEST)
        {
            if (tile->w_tile != nullptr)
                x_target -= 1;
            else
                return;
        }
        MapObject* targetCellContent = game_state->current_field->field[x_target][y_target]->content;
        if (targetCellContent->id == '0' || targetCellContent->id == 'f' || game_state->is_consumable(targetCellContent->id))
        {
            delete game_state->current_field->field[x_target][y_target]->content;
            Fire* fire = new Fire('f', x_target, y_target);
            game_state->current_field->field_chars[x_target][y_target] = fire->id;
            game_state->current_field->field[x_target][y_target]->content = fire;
        }
        else if (targetCellContent->id == '1' || targetCellContent->id == '2' || targetCellContent->id == '3' || targetCellContent->id == '4')
            static_cast<Player*>(targetCellContent)->take_damage(value, game_state);
        else if (targetCellContent->id == '-')
            return;
    }
}

/**
 * \brief shoots in range of skill in chosen direction
 * then adds fire on target tile and neighbouring tiles
 */
void Skill::rocket_type(Player* player, Direction direction, GameState* game_state)
{
    std::vector<std::vector<int>> targets;
    targets.resize(3);
    targets[0].resize(2);
    targets[1].resize(2);
    targets[2].resize(2);
    if (direction == Direction::DIR_NORTH)
    {
        targets[0][0] = player->x_pos;
        targets[0][1] = player->y_pos - range;
    }
    else if (direction == Direction::DIR_EAST)
    {
        targets[0][0] = player->x_pos + range;
        targets[0][1] = player->y_pos;
    }
    else if (direction == Direction::DIR_SOUTH)
    {
        targets[0][0] = player->x_pos;
        targets[0][1] = player->y_pos + range;
    }
    else if (direction == Direction::DIR_WEST)
    {
        targets[0][0] = player->x_pos - range;
        targets[0][1] = player->y_pos;
    }
    if (direction == Direction::DIR_NORTH || direction == Direction::DIR_SOUTH)
    {
        targets[1][0] = targets[0][0] + 1;
        targets[1][1] = targets[0][1];
        targets[2][0] = targets[0][0] - 1;
        targets[2][1] = targets[0][1];
    }
    else if (direction == Direction::DIR_EAST || direction == Direction::DIR_WEST)
    {
        targets[1][0] = targets[0][0];
        targets[1][1] = targets[0][1] + 1;
        targets[2][0] = targets[0][0];
        targets[2][1] = targets[0][1] - 1;
    }
    for (auto t : targets)
    {
	    if(t[0] >= 0 && t[0] < game_state->current_field->dimension && t[1] >= 0 && t[1] < game_state->current_field->dimension)
	    {
            MapObject* targetCellContent = game_state->current_field->field[t[0]][t[1]]->content;
            if(targetCellContent->id == '0' || targetCellContent->id == 'f' || game_state->is_consumable(targetCellContent->id))
            {
                delete game_state->current_field->field[t[0]][t[1]]->content;
                Fire* fire = new Fire('f', t[0], t[1]);
                game_state->current_field->field_chars[t[0]][t[1]] = fire->id;
                game_state->current_field->field[t[0]][t[1]]->content = fire;
            }
            else if (targetCellContent->id == '1' || targetCellContent->id == '2' || targetCellContent->id == '3' || targetCellContent->id == '4')
                static_cast<Player*>(targetCellContent)->take_damage(value, game_state);
	    }
    }
}

/**
 * \brief pushes players next to target tile to one further tile away
 */
void Skill::push_type(Player* player, Direction direction, GameState* game_state)
{
    int x_target = 0;
    int y_target = 0;
    if (direction == Direction::DIR_NORTH)
    {
        x_target = player->x_pos;
        y_target = player->y_pos - range;
    }
    else if (direction == Direction::DIR_EAST)
    {
        x_target = player->x_pos + range;
        y_target = player->y_pos;
    }
    else if (direction == Direction::DIR_SOUTH)
    {
        x_target = player->x_pos;
        y_target = player->y_pos + range;
    }
    else if (direction == Direction::DIR_WEST)
    {
        x_target = player->x_pos - range;
        y_target = player->y_pos;
    }
    if(x_target >= 0 && x_target < game_state->current_field->dimension && y_target >= 0 && y_target < game_state->current_field->dimension)
    {
        Tile* tile = game_state->current_field->field[x_target][y_target];
        if (tile->n_tile != nullptr)
        {
            Tile* nTile = tile->n_tile;
            MapObject* targetCellContent = nTile->content;
            if ((targetCellContent->id == '1' || targetCellContent->id == '2' || targetCellContent->id == '3' || targetCellContent->id == '4') && nTile->n_tile != nullptr)
                game_state->move_to_tile(static_cast<Player*>(targetCellContent), targetCellContent->x_pos, targetCellContent->y_pos - 1);
        }
        if (tile->e_tile != nullptr)
        {
            Tile* eTile = tile->e_tile;
            MapObject* targetCellContent = eTile->content;
            if ((targetCellContent->id == '1' || targetCellContent->id == '2' || targetCellContent->id == '3' || targetCellContent->id == '4') && eTile->e_tile != nullptr)
                game_state->move_to_tile(static_cast<Player*>(targetCellContent), targetCellContent->x_pos + 1, targetCellContent->y_pos);
        }
        if (tile->s_tile != nullptr)
        {
            Tile* sTile = tile->s_tile;
            MapObject* targetCellContent = sTile->content;
            if ((targetCellContent->id == '1' || targetCellContent->id == '2' || targetCellContent->id == '3' || targetCellContent->id == '4') && sTile->s_tile != nullptr)
                game_state->move_to_tile(static_cast<Player*>(targetCellContent), targetCellContent->x_pos, targetCellContent->y_pos + 1);
        }
        if (tile->w_tile != nullptr)
        {
            Tile* wTile = tile->w_tile;
            MapObject* targetCellContent = wTile->content;
            if ((targetCellContent->id == '1' || targetCellContent->id == '2' || targetCellContent->id == '3' || targetCellContent->id == '4') && wTile->w_tile != nullptr)
                game_state->move_to_tile(static_cast<Player*>(targetCellContent), targetCellContent->x_pos - 1, targetCellContent->y_pos);
        }
    }
}

/**
 * \brief deals damage in chosen direction.\n
 * when hitting a wall the wall gets destroyed.\n
 * when hitting a second wall the skill stops.
 */
void Skill::break_type(Player* player, Direction direction, GameState* game_state)
{
    int x_target = player->x_pos;
    int y_target = player->y_pos;
    Tile* tile = nullptr;
    bool broke = false;
    for (int i = 0; i < range; i++)
    {
        tile = game_state->current_field->field[x_target][y_target];
        if (direction == Direction::DIR_NORTH)
        {
            if (tile->n_tile != nullptr)
                y_target -= 1;
            else
                return;
        }
        if (direction == Direction::DIR_EAST)
        {
            if (tile->e_tile != nullptr)
                x_target += 1;
            else
                return;
        }
        if (direction == Direction::DIR_SOUTH)
        {
            if (tile->s_tile != nullptr)
                y_target += 1;
            else
                return;
        }
        if (direction == Direction::DIR_WEST)
        {
            if (tile->w_tile != nullptr)
                x_target -= 1;
            else
                return;
        }
        MapObject* targetCellContent = game_state->current_field->field[x_target][y_target]->content;
        if (targetCellContent->id == '1' || targetCellContent->id == '2' || targetCellContent->id == '3' || targetCellContent->id == '4')
            static_cast<Player*>(targetCellContent)->take_damage(value, game_state);
        else if (targetCellContent->id == '-')
        {
            if(broke)
				return;
            static_cast<Wall*>(targetCellContent)->take_damage(WALL_HP, game_state);
            broke = true;
        }
    }
}

Skill::~Skill()
{
	
}
