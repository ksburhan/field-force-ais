#include "client.h"
#include "clientsend.h"

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
		std::cout << "GAMEMODE" << std::endl;
		break;
	case PLAYERINFORMATION:
		std::cout << "PLAYERINFO" << std::endl;
		break;
	case GAMEFIELD:
		std::cout << "GAMEFIELD" << std::endl;
		break;
	case MOVEREQUEST:
		std::cout << "MOVEREQUEST" << std::endl;
		break;
	case NEWGAMESTATE:
		std::cout << "NEWGAMESTATE" << std::endl;
		break;
	case MOVEDISTRIBUTION:
		std::cout << "MOVEDISTRIBUTION" << std::endl;
		break;
	case GAMEERROR:
		std::cout << "GAMEERROR" << std::endl;
		break;
	case GAMEOVER:
		std::cout << "GAMEOVER" << std::endl;
		break;
	default:
		std::cerr << "ERROR: No valid packet type detected " << type << std::endl;
		break;
	}
}


void Client::sendPacket(Packet packet)
{
	int sendResult = send(sock, reinterpret_cast<const char*>(packet.buffer.data()), packet.buffer.size(), 0);
	std::cout << sendResult << std::endl;
}
