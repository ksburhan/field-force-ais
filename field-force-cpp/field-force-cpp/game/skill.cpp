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

void Skill::setOnCooldown()
{
	cooldown_left = cooldown;
}

void Skill::prepareForNextRound()
{
	if(cooldown_left > 0)
	{
		cooldown_left--;
	}
}

void Skill::useSkill(Player* player, Direction direction, GameState* game_state)
{
    switch (type)
    {
    case MOVEMENT:
        movementType(player, direction, game_state);
        break;
    case REGENERATE:
        regenerateType(player, direction, game_state);
        break;
    case FIRE:
        fireType(player, direction, game_state);
        break;
    case ROCKET:
        rocketType(player, direction, game_state);
        break;
    case PUSH:
        pushType(player, direction, game_state);
        break;
    case BREAK:
        breakType(player, direction, game_state);
        break;
    }
}

void Skill::movementType(Player* player, Direction direction, GameState* game_state)
{
    for (int i = 0; i < range; i++)
    {
        Tile* tile = &game_state->current_field.field[player->x_pos][player->y_pos];
        if (direction == Direction::DIR_NORTH)
        {
            if (tile->nTile == nullptr)
            {
                game_state->moveToTile(player, player->x_pos, player->y_pos - 1);
            }
            else
                return;
        }
        if (direction == Direction::DIR_EAST)
        {
            if (tile->eTile == nullptr)
            {
                game_state->moveToTile(player, player->x_pos + 1, player->y_pos);
            }
            else
                return;
        }
        if (direction == Direction::DIR_SOUTH)
        {
            if (tile->sTile == nullptr)
            {
                game_state->moveToTile(player, player->x_pos, player->y_pos + 1);
            }
            else
                return;
        }
        if (direction == Direction::DIR_WEST)
        {
            if (tile->wTile == nullptr)
            {
                game_state->moveToTile(player, player->x_pos - 1, player->y_pos);
            }
            else
                return;
        }
    }
}

void Skill::regenerateType(Player* player, Direction direction, GameState* game_state)
{
    player->heal(value);
}

void Skill::fireType(Player* player, Direction direction, GameState* game_state)
{
    int x_target = player->x_pos;
    int y_target = player->y_pos;
    Tile* tile;
    for (int i = 0; i < range; i++)
    {
        tile = &game_state->current_field.field[x_target][y_target];
        if (direction == Direction::DIR_NORTH)
        {
            if (tile->nTile == nullptr)
            {
                y_target -= i;
            }
            else
                return;
        }
        if (direction == Direction::DIR_EAST)
        {
            if (tile->eTile == nullptr)
            {
                x_target += i;
            }
            else
                return;
        }
        if (direction == Direction::DIR_SOUTH)
        {
            if (tile->sTile == nullptr)
            {
                y_target += i;
            }
            else
                return;
        }
        if (direction == Direction::DIR_WEST)
        {
            if (tile->wTile == nullptr)
            {
                x_target -= i;
            }
            else
                return;
        }
        MapObject* targetCellContent = &game_state->current_field.field[x_target][y_target].content;
        if (targetCellContent->id == '0') // TODO: INSTANCE OF FIRE OR CONSUMABLE
        {
            Fire fire('f', x_target, y_target);
            game_state->current_field.field_chars[x_target][y_target] = fire.id;
            game_state->current_field.field[x_target][y_target].content = fire;
        }
        else if (targetCellContent->id == '1') // TODO: INSTANCE OF PLAYER
        {
            //(Player*)targetCellContent.takeDamage(value, game_state);
        }
        else if (targetCellContent->id == '-') // TODO: INSTANCE OF WALL
        {
            return;
        }
    }
}

void Skill::rocketType(Player* player, Direction direction, GameState* game_state)
{

}

void Skill::pushType(Player* player, Direction direction, GameState* game_state)
{

}

void Skill::breakType(Player* player, Direction direction, GameState* game_state)
{

}