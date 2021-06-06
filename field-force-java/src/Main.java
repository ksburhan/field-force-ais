import AI.AI;
import Connection.Client;

public class Main {
    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 26353;

        String playername = "playername";

        for ( int i = 0; i < args.length; i++ ) {
            if ( args[i].equals("-i") ) {
                i++;
                ip = args[i];
            } else if ( args[i].equals("-p") ) {
                i++;
                port = Integer.parseInt(args[i]);
            } else if ( args[i].equals("-n") ) {
                i++;
                playername = args[i];
            }else if ( args[i].equals("-h") ) {
                System.out.println("-i <ip>     Set ip to connect to");
                System.out.println("-p <port>   Set port to connect to");
                System.out.println("-n <name>   Set playername");
                System.out.println("-h          Show help");

            }

        }
        AI ai = AI.getInstance();
        Client client = Client.getInstance();
        client.initConnection(ip, port);
    }
}
