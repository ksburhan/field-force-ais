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

GameField::GameField(int _dimension, std::vector<std::vector<char>> _map)
{
	dimension = _dimension;
	field_chars = _map;
	field = createField(_map);
}

std::vector<std::vector<Tile>> GameField::createField(std::vector<std::vector<char>> map)
{
	std::vector<std::vector<Tile>> field;
    field.resize(dimension);
    for (int i = 0; i < dimension; i++)
        field[i].resize(dimension);
    for (int y = 0; y < dimension; y++)
    {
        for (int x = 0; x < dimension; x++)
        {
            MapObject mapObject;
            switch (map[x][y]) {
            case '1':
                mapObject = ALL_PLAYERS.at(0);
                mapObject.setPos(x, y);
                break;
            case '2':
                mapObject = ALL_PLAYERS.at(0);
                mapObject.setPos(x, y);
                break;
            case '3':
                mapObject = ALL_PLAYERS.at(0);
                mapObject.setPos(x, y);
                break;
            case '4':
                mapObject = ALL_PLAYERS.at(0);
                mapObject.setPos(x, y);
                break;
            case 'f':
                mapObject = Fire(map[x][y], x, y);
                break;
            case '-':
                mapObject = Wall(map[x][y], x, y);
                break;
            case '0':
                mapObject = MapObject(map[x][y], x, y);
                break;
            default:
                for (auto c : ALL_CONSUMABLES)
                {
                    if (c.id == map[x][y])
                        mapObject = Consumable(map[x][y], x, y);
                    break;
                }
                break;
            }
            field[x][y] = Tile(x, y, mapObject);
        }
    }

    for (int y = 0; y < dimension; y++) {
        for (int x = 0; x < dimension; x++) {
            if (y > 0)
                field[x][y].nTile = &field[x][y - 1];
            if (x != dimension - 1)
                field[x][y].eTile = &field[x + 1][y];
            if (y != dimension - 1)
                field[x][y].sTile = &field[x][y + 1];
            if (x > 0)
                field[x][y].wTile = &field[x - 1][y];
        }
    }
	return field;
}

void GameField::printMap()
{
    for (int y = 0; y < dimension; y++) {
        for (int x = 0; x < dimension; x++)
            std::cout << field_chars[x][y] << " ";
        std::cout << std::endl;
    }
}
