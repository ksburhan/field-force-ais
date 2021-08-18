#include <iostream>
#include <string>

#include "connection/client.h"
using namespace std;
int main(int argc, char* argv[])
{
	string ip = "127.0.0.1";
    int port = 26353;
    string name = "c++";
    int skill1 = 0;
    int skill2 = 1;
    bool verbose = false;
    for (int i = 1; i < argc; ++i) {
        string arg = argv[i];
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
            name = argv[i];
        }
        else if (arg == "-s1")
        {
            i++;
            skill1 = stoi(argv[i]);
        }
        else if (arg == "-s2")
        {
            i++;
            skill2 = stoi(argv[i]);
            if (skill1 == skill2)
            {
                cerr << "You can't use the same skill twice!" << endl;
                return -1;
            }
        }
        else if (arg == "-v")
            verbose = true;
        else if (arg == "-h")
        {
            cout << "-i <ip>                  Set ip to connect to" << endl
                 << "-p <port>                Set port to connect to" << endl
                 << "-n <name>                Set playername" << endl
                 << "-s1 <skill_id>           Set skill 1" << endl
                 << "-s2 <skill_id>           Set skill 2" << endl
                 << "-v                       Set verbose on" << endl
                 << "-h                       Show help" << endl;
            return 0;
        }
        Client client;
    }
}
