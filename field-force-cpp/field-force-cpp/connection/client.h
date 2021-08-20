#pragma once
#include <string>

#include "packet.h"

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

class Client
{
public:
	static Client& getInstance();
private:
	Client() {}

	std::string ip = "127.0.0.1";
	int port = 26353;

	bool game_is_running = false;

	void disconnect();
public:
	void conn(std::string, int);
	void sendPacket(Packet);
	void handleMessage(int, Packet);

	Client(Client const&) = delete;
	void operator=(Client const&) = delete;
#ifdef _WIN32
	SOCKET sock = NULL;
#else
	int sock;
#endif
};
