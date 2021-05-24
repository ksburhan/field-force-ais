public class Main {
    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 26353;

        for ( int i = 0; i < args.length; i++ ) {
            if ( args[i].equals("-i") ) {
                i++;
                ip = args[i];
            } else if ( args[i].equals("-p") ) {
                i++;
                port = Integer.parseInt(args[i]);
            }else if ( args[i].equals("-h") ) {
                System.out.println("-i <ip>     Set ip to connect to");
                System.out.println("-p <port>   Set port to connect to");
                System.out.println("-h          Show help");

            }

        }
        Client client = Client.getInstance();
        client.initConnection(ip, port);
    }
}
