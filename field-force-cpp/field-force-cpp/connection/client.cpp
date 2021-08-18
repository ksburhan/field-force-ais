#include "client.h"

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
Client::Client()
{
    ip = "";
    port = 0;
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

	SOCKET sock = socket(AF_INET, SOCK_STREAM, 0);
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
		closesocket(sock);
		WSACleanup();
		return;
	}

	char buf[4096];
	std::string userInput;

	bool game_is_running = true;

	/*while (game_is_running)
	{
		int length;
		recv(sock, (char *)length, sizeof(int), 0);
		int type;
		recv(sock, (char *)type, sizeof(int), 0);
	}*/

	do
	{
		std::cout << "> ";
		getline(std::cin, userInput);

		if (userInput.size() > 0)
		{
			int sendResult = send(sock, userInput.c_str(), userInput.size() + 1, 0);
			if (sendResult != SOCKET_ERROR)
			{
				ZeroMemory(buf, 4096);
				int bytesReceived = recv(sock, buf, 4096, 0);
				if (bytesReceived > 0)
				{
					std::cout << std::string(buf, 0, bytesReceived) << std::endl;
				}
			}
		}

	} while (userInput.size() > 0);

	closesocket(sock);
	WSACleanup();
}
#endif

