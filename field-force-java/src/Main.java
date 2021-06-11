import AI.AI;
import Connection.Client;

public class Main {
    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 26353;

        AI ai = AI.getInstance();
        for ( int i = 0; i < args.length; i++ ) {
            switch (args[i]) {
                case "-i" -> {
                    i++;
                    ip = args[i];
                }
                case "-p" -> {
                    i++;
                    port = Integer.parseInt(args[i]);
                }
                case "-n" -> {
                    i++;
                    AI.playername = args[i];
                }
                case "-h" -> {
                    System.out.println("-i <ip>     Set ip to connect to");
                    System.out.println("-p <port>   Set port to connect to");
                    System.out.println("-n <name>   Set playername");
                    System.out.println("-h          Show help");
                }
            }

        }
        Client client = Client.getInstance();
        client.initConnection(ip, port);
    }
}
