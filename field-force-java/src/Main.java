import AI.AI;
import AI.Constants;
import Connection.Client;

public class Main {
    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 26353;
        AI.getInstance();
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
                case "-s1" -> {
                    i++;
                    AI.skill1 = Integer.parseInt(args[i]);
                }
                case "-s2" -> {
                    i++;
                    AI.skill2 = Integer.parseInt(args[i]);
                    if(AI.skill1 == AI.skill2){
                        System.out.println("You can't have the same skill twice");
                        return;
                    }
                }
                case "-v" -> {
                    i++;
                    Constants.VERBOSE = true;
                }
                case "-h" -> {
                    System.out.println("-i <ip>                 Set ip to connect to\n" +
                                       "-p <port>               Set port to connect to" +
                                       "-n <name>               Set playername" +
                                       "-s1 <skill_id>          Set skill 1" +
                                       "-s2 <skill_id>          Set skill 2" +
                                       "-v                      Set verbose on" +
                                       "-h                      Show help");
                    return;
                }
            }

        }
        Client client = Client.getInstance();
        client.initConnection(ip, port);
    }
}
