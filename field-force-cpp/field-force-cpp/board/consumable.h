#pragma once
#include <string>
#include <vector>

#include "mapobject.h"

class Consumable: public MapObject
{
public:
	std::string con_name = "";
	int healing = 0;
	int shield = 0;

	Consumable();
	Consumable(char, int, int);
	Consumable(char, std::string, int, int);

	Consumable getConsumable(char);
};

inline std::vector<Consumable> ALL_CONSUMABLES;