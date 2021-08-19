#include "client.h"
#include "clientsend.h"
#include "clienthandle.h"

#include <iostream>

#ifdef _WIN32
#include <WS2tcpip.h>
#pragma comment (lib, "ws2_32.lib")
#else
#include<unistd.h>
#include<sys/socket.h>
#include<sys/types.h>
#include<netdb.h>
#include<arpa/inet.h>
#endif

Client& Client::getInstance()
{
	static Client instance;

	return instance;
}

void Client::conn(std::string _ip, int _port)
{
	ip = _ip;
	port = _port;
#ifdef _WIN32
	win_conn();
#endif
}

#ifdef _WIN32
void Client::win_conn()
{
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

	sockaddr_in hint;
	hint.sin_family = AF_INET;
	hint.sin_port = htons(port);
	inet_pton(AF_INET, ip.c_str(), &hint.sin_addr);

	int connResult = connect(sock, (sockaddr*)&hint, sizeof(hint));
	if (connResult == SOCKET_ERROR)
	{
		std::cerr << "Couldn't connect to server, Errorcode: " << WSAGetLastError() << std::endl;
		disconnect();
		return;
	}

	sendPlayername();
	game_is_running = true;

	while (game_is_running)
	{
		uint8_t* lengthB = new uint8_t[4];
		int bytesReceived = recv(sock, (char*)lengthB, sizeof(int), MSG_WAITALL);
		int length = Packet::convertByteArrayToInt(lengthB);
		std::cout << length << std::endl;
		if (bytesReceived <= 0)
		{
			std::cerr << "Couldn't read message length!" << WSAGetLastError() << std::endl;
			disconnect();
			return;
		}
		uint8_t* typeB = new uint8_t[4];
		bytesReceived = recv(sock, (char*)typeB, sizeof(int), MSG_WAITALL);
		int type = Packet::convertByteArrayToInt(typeB);
		std::cout << type << std::endl;
		if (bytesReceived <= 0)
		{
			std::cerr << "Couldn't read message type!" << std::endl;
			disconnect();
			return;
		}
		uint8_t* data = new uint8_t[length-4];
		bytesReceived = recv(sock, (char*)data, length - 4, MSG_WAITALL);
		std::cout << bytesReceived << std::endl;
		if (bytesReceived <= 0)
		{
			std::cerr << "Couldn't read packet data!" << std::endl;
			disconnect();
			return;
		}
		Packet packet(data);
		handleMessage(type, packet);
	}
	disconnect();
}
#endif

void Client::disconnect()
{
	closesocket(sock);
	WSACleanup();
}

void Client::handleMessage(int type, Packet packet)
{
	switch (type)
	{
	case GAMEMODE:
		try
		{
			std::cerr << "Type 2" << std::endl;
			handleGamemode(packet);
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
			std::cerr << "Type 3" << std::endl;
			handlePlayerinformation(packet);
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
			std::cerr << "Type 4" << std::endl;
			handleInitialMap(packet);
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
			std::cerr << "Type 5" << std::endl;
			handleMoveRequest(packet);
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
			std::cerr << "Type 7" << std::endl;
			handleNewGamestate(packet);
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
			std::cerr << "Type 8" << std::endl;
			handleMovedistribution(packet);
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
			std::cerr << "Type 9" << std::endl;
			handleError(packet);
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
			std::cerr << "Type 10" << std::endl;
			handleGameover(packet);
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


void Client::sendPacket(Packet packet)
{
	int sendResult = send(sock, reinterpret_cast<const char*>(packet.buffer.data()), packet.buffer.size(), 0);
	std::cout << sendResult << std::endl;
}
