#include "consumable.h"

Consumable::Consumable() { }

Consumable::Consumable(char _id, int _x_pos, int _y_pos)
{
	id = _id;
	x_pos = _x_pos;
	y_pos = _y_pos;
	Consumable c = ALL_CONSUMABLES.at(_id);
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
