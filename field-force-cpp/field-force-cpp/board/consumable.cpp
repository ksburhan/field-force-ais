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
#include "consumable.h"

Consumable::Consumable() { }

Consumable::Consumable(char _id, int _x_pos, int _y_pos)
{
	id = _id;
	x_pos = _x_pos;
	y_pos = _y_pos;
	Consumable c = get_consumable(_id);
	con_name = c.con_name;
	healing = c.healing;
	shield = c.shield;
}

Consumable::Consumable(char _id, std::string _con_name, int _healing, int _shield)
{
	id = _id;
	con_name = _con_name;
	healing = _healing;
	shield = _shield;
}

Consumable Consumable::get_consumable(char _id)
{
	for (auto c : ALL_CONSUMABLES)
	{
		if(c.id == _id)
		{
			return c;
		}
	}
}