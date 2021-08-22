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
#include "clientsend.h"

#include "client.h"
#include "packet.h"
#include "../game/gameconstants.h"

void sendPacket(Packet packet)
{
	packet.writeLength();
	Client& client = Client::getInstance();
	client.sendPacket(packet);
}

void sendPlayername()
{
	Packet packet;
	packet.write(PLAYERNAME);
	packet.write(playername);
	packet.write(skill1);
	packet.write(skill2);
	sendPacket(packet);
}

void sendMovereply(int id, Move* move)
{
	Packet packet;
	packet.write(MOVEREPLY);
	packet.write(id);
	packet.write(*move);
	if(move->type == MoveType::MT_SKILL)
	{
		move->skill.setOnCooldown();
	}
	sendPacket(packet);
}