#pragma once
#include "packet.h"

void handleGamemode(Packet);
void handlePlayerinformation(Packet);
void handleInitialMap(Packet);
void handleMoveRequest(Packet);
void handleNewGamestate(Packet);
void handleMovedistribution(Packet);
void handleError(Packet);
void handleGameover(Packet);
