#include "consumable.h"

Consumable::Consumable() { }

Consumable::Consumable(char _id, std::string _con_name, int _healing, int _shield)
{
	id = _id;
	con_name = _con_name;
	healing = _healing;
	shield = _shield;
}
