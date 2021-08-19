#pragma once
#include <string>
#include <vector>

#include "mapobject.h"

class Consumable: public MapObject
{
public:
	static std::vector<Consumable> ALL_CONSUMABLES;
	std::string con_name;
	int healing;
	int shield;

	Consumable();
	Consumable(char, std::string, int, int);
};
