#pragma once

class MapObject
{
public:
	char id = '.';
	int x_pos = -1;
	int y_pos = -1;

	MapObject();
	MapObject(char, int, int);

	void setPos(int, int);
};