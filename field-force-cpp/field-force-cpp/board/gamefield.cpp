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
#include "gamefield.h"

#include <iostream>

#include "consumable.h"
#include "fire.h"
#include "wall.h"
#include "../game/player.h"

GameField::GameField() { }

GameField::GameField(int _dimension, std::vector<std::vector<char>> _map, std::vector<Player>* current_players)
{
	dimension = _dimension;
	field_chars = _map;
	field = createField(_map, *current_players);
}

std::vector<std::vector<Tile*>> GameField::createField(std::vector<std::vector<char>> map, const std::vector<Player> &current_players)
{
	std::vector<std::vector<Tile*>> field;
    field.resize(dimension);
    for (int i = 0; i < dimension; i++)
        field[i].resize(dimension);
    for (int y = 0; y < dimension; y++)
    {
        for (int x = 0; x < dimension; x++)
        {
            MapObject* mapObject = nullptr;
            Player p;
            switch (map[x][y]) {
            case '1':
                p = get_player(1, current_players);
                mapObject = new Player(p.id, p.player_number, p.playername, p.hp, p.shield, p.x_pos, p.y_pos, p.skill1, p.skill2);
                mapObject->set_pos(x, y);
                break;
            case '2':
                p = get_player(2, current_players);
                mapObject = new Player(p.id, p.player_number, p.playername, p.hp, p.shield, p.x_pos, p.y_pos, p.skill1, p.skill2);
                mapObject->set_pos(x, y);
                break;
            case '3':
                p = get_player(3, current_players);
                mapObject = new Player(p.id, p.player_number, p.playername, p.hp, p.shield, p.x_pos, p.y_pos, p.skill1, p.skill2);
                mapObject->set_pos(x, y);
                break;
            case '4':
                p = get_player(4, current_players);
                mapObject = new Player(p.id, p.player_number, p.playername, p.hp, p.shield, p.x_pos, p.y_pos, p.skill1, p.skill2);
                mapObject->set_pos(x, y);
                break;
            case 'f':
                mapObject = new Fire(map[x][y], x, y);
                break;
            case '-':
                mapObject = new Wall(map[x][y], x, y);
                break;
            case '0':
                mapObject = new MapObject(map[x][y], x, y);
                break;
            default:
                for (auto c : ALL_CONSUMABLES)
                {
                    if (c.id == map[x][y])
                    {
                        mapObject = new Consumable(map[x][y], x, y);
                    }
                }
                break;
            }
            field[x][y] = new Tile(x, y, mapObject);
        }
    }

    for (int y = 0; y < dimension; y++) {
        for (int x = 0; x < dimension; x++) {
            if (y > 0)
                field[x][y]->n_tile = field[x][y - 1];
            if (x != dimension - 1)
                field[x][y]->e_tile = field[x + 1][y];
            if (y != dimension - 1)
                field[x][y]->s_tile = field[x][y + 1];
            if (x > 0)
                field[x][y]->w_tile = field[x - 1][y];
        }
    }
	return field;
}

Player GameField::get_player(int id, const std::vector<Player>& current_player)
{
	for (const auto& p : current_player)
	{
        if (id == p.player_number)
            return p;
	}
}


void GameField::print_map()
{
    for (int y = 0; y < dimension; y++) {
        for (int x = 0; x < dimension; x++)
            std::cout << field_chars[x][y] << " ";
        std::cout << std::endl;
    }
}

GameField::~GameField()
{
    for (int y = 0; y < dimension; y++)
        for (int x = 0; x < dimension; x++)
            delete field[x][y];
}
