#pragma once
#include "packet.h"

void handle_gamemode(Packet);
void handle_playerinformation(Packet);
void handle_initial_map(Packet);
void handle_move_request(Packet);
void handle_new_gamestate(Packet);
void handle_movedistribution(Packet);
void handle_error(Packet);
void handle_gameover(Packet);
