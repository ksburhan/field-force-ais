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
#include "client.h"
#include "clientsend.h"
#include "clienthandle.h"
#include "../ai/ai.h"

#include <iostream>

#ifdef _WIN32
#include <WS2tcpip.h>
#pragma comment (lib, "ws2_32.lib")
#else
#include <sys/types.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>
#endif

Client& Client::get_instance()
{
	static Client instance;

	return instance;
}

void Client::conn(std::string _ip, int _port)
{
	ip = _ip;
	port = _port;

#ifdef _WIN32
	WSADATA wsaData;
	WORD ver = MAKEWORD(2, 2);
	int wsResult = WSAStartup(ver, &wsaData);
	if (wsResult != 0)
	{
		std::cerr << "Couldn't start Winsock, Errorcode: " << wsResult << std::endl;
		return;
	}

	sock = socket(AF_INET, SOCK_STREAM, 0);
	if (sock == INVALID_SOCKET)
	{
		std::cerr << "Couldn't create socket, Errorcode: " << WSAGetLastError() << std::endl;
		WSACleanup();
		return;
	}


#else
	sock = socket(AF_INET, SOCK_STREAM, 0);
	if (sock == -1)
	{
		return;
	}
#endif

	sockaddr_in hint;
	hint.sin_family = AF_INET;
	hint.sin_port = htons(port);
	inet_pton(AF_INET, ip.c_str(), &hint.sin_addr);

	int connResult = connect(sock, (sockaddr*)&hint, sizeof(hint));
	if (connResult == -1)
	{
		std::cerr << "Couldn't connect to server!" << std::endl;
		disconnect();
		return;
	}

	send_playername();
	game_is_running = true;

	while (game_is_running)
	{
		uint8_t* lengthB = new uint8_t[4];
		int bytesReceived = recv(sock, (char*)lengthB, sizeof(int), MSG_WAITALL);
		int length = Packet::convert_byte_array_to_int(lengthB);
		delete[] lengthB;
		std::cout << length << std::endl;
		if (bytesReceived <= 0)
		{
			std::cerr << "Couldn't read message length!" << std::endl;
			disconnect();
			return;
		}
		uint8_t* typeB = new uint8_t[4];
		bytesReceived = recv(sock, (char*)typeB, sizeof(int), MSG_WAITALL);
		int type = Packet::convert_byte_array_to_int(typeB);
		delete[] typeB;
		std::cout << type << std::endl;
		if (bytesReceived <= 0)
		{
			std::cerr << "Couldn't read message type!" << std::endl;
			disconnect();
			return;
		}
		std::vector<uint8_t> data;
		data.resize(length - 4);
		bytesReceived = recv(sock, (char *)&data[0], length - 4, MSG_WAITALL);
		std::cout << bytesReceived << std::endl;
		if (bytesReceived <= 0)
		{
			std::cerr << "Couldn't read packet data!" << std::endl;
			disconnect();
			return;
		}
		Packet packet(data);
		handle_message(type, packet);
	}
	disconnect();
}

void Client::disconnect()
{
#ifdef _WIN32
	closesocket(sock);
	WSACleanup();
#else
	close(sock);
#endif
}

void Client::handle_message(int type, Packet packet)
{
	switch (type)
	{
	case GAMEMODE:
		try
		{
			std::cout << "Type 2" << std::endl;
			handle_gamemode(packet);
		} catch( std::exception &e)
		{
			std::cerr << e.what() << std::endl;
			disconnect();
			exit(-2);
		}
		break;
	case PLAYERINFORMATION:
		try
		{
			std::cout << "Type 3" << std::endl;
			handle_playerinformation(packet);
		}
		catch (std::exception& e)
		{
			std::cerr << e.what() << std::endl;
			disconnect();
			exit(-3);
		}
		break;
	case GAMEFIELD:
		try
		{
			std::cout << "Type 4" << std::endl;
			handle_initial_map(packet);
			AI& ai = AI::get_instance();
			ai.current_gamestate->current_field->print_map();
		}
		catch (std::exception& e)
		{
			std::cerr << e.what() << std::endl;
			disconnect();
			exit(-4);
		}
		break;
	case MOVEREQUEST:
		try
		{
			std::cout << "Type 5" << std::endl;
			handle_move_request(packet);
		}
		catch (std::exception& e)
		{
			std::cerr << e.what() << std::endl;
			disconnect();
			exit(-5);
		}
		break;
	case NEWGAMESTATE:
		try
		{
			std::cout << "Type 7" << std::endl;
			handle_new_gamestate(packet);
			AI& ai = AI::get_instance();
			ai.current_gamestate->current_field->print_map();
		}
		catch (std::exception& e)
		{
			std::cerr << e.what() << std::endl;
			disconnect();
			exit(-7);
		}
		break;
	case MOVEDISTRIBUTION:
		try
		{
			std::cout << "Type 8" << std::endl;
			handle_movedistribution(packet);
		}
		catch (std::exception& e)
		{
			std::cerr << e.what() << std::endl;
			disconnect();
			exit(-8);
		}
		break;
	case GAMEERROR:
		try
		{
			std::cout << "Type 9" << std::endl;
			handle_error(packet);
			disconnect();
			exit(-9);
		}
		catch (std::exception& e)
		{
			std::cerr << e.what() << std::endl;
			disconnect();
			exit(-9);
		}
		break;
	case GAMEOVER:
		try
		{
			std::cout << "Type 10" << std::endl;
			handle_gameover(packet);
			game_is_running = false;
		}
		catch (std::exception& e)
		{
			std::cerr << e.what() << std::endl;
			disconnect();
			exit(-10);
		}
		break;
	default:
		std::cerr << "ERROR: No valid packet type detected " << type << std::endl;
		disconnect();
		exit(-11);
		break;
	}
}


void Client::send_packet(Packet packet)
{
	int sendResult = send(sock, reinterpret_cast<const char*>(packet.buffer.data()), packet.buffer.size(), 0);
	std::cout << sendResult << std::endl;
}
