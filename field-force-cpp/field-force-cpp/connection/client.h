#pragma once
#include <string>
class Client
{
private:
	std::string ip;
	int port;
public:
	Client();
	void conn(std::string, int);
#ifdef _WIN32
	void win_conn();
#endif
};