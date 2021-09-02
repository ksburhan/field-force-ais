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
#include <iostream>
#include <string>

#include "connection/client.h"
#include "game/gameconstants.h"
int main(int argc, char* argv[])
{
#ifdef _WIN32
    _CrtSetDbgFlag(_CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);
#endif
	std::string ip = "127.0.0.1";
    int port = 26353;
    for (int i = 1; i < argc; ++i) {
        std::string arg = argv[i];
        if (arg == "-i") 
        {
            i++;
            ip = argv[i];
        }
        else if (arg == "-p")
        {
            i++;
            ip = argv[i];
        }
        else if (arg == "-n")
        {
            i++;
            playername = argv[i];
        }
        else if (arg == "-s1")
        {
            i++;
            skill1 = std::stoi(argv[i]);
        }
        else if (arg == "-s2")
        {
            i++;
            skill2 = std::stoi(argv[i]);
            if (skill1 == skill2)
            {
                std::cerr << "You can't use the same skill twice!" << std::endl;
                return -1;
            }
        }
        else if (arg == "-v")
            VERBOSE = true;
        else if (arg == "-h")
        {
            std::cout << "-i <ip>                  Set ip to connect to" << std::endl
					  << "-p <port>                Set port to connect to" << std::endl
					  << "-n <name>                Set playername" << std::endl
					  << "-s1 <skill_id>           Set skill 1" << std::endl
					  << "-s2 <skill_id>           Set skill 2" << std::endl
					  << "-v                       Set verbose on" << std::endl
					  << "-h                       Show help" << std::endl;
            return 0;
        }
    }
    Client& client = Client::get_instance();
	client.conn(ip, port);
#ifdef _WIN32
    _CrtDumpMemoryLeaks();
#endif
    return 0;
}
