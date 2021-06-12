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
                case "-h" -> {
                    System.out.println("-i <ip>                 Set ip to connect to");
                    System.out.println("-p <port>               Set port to connect to");
                    System.out.println("-n <name>               Set playername");
                    System.out.println("-s1 <skill_id>          Set skill 1");
                    System.out.println("-s2 <skill_id>          Set skill 2");
                    System.out.println("-v                      Set verbose on");
                    System.out.println("-h                      Show help");
                }
            }

        }
        Client client = Client.getInstance();
        client.initConnection(ip, port);
    }
}
