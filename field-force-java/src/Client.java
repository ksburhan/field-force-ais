import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import static java.lang.System.exit;
import static java.lang.System.out;

public class Client {

    private static Client instance;

    private String ip;
    private int port;
    private DataOutputStream output;
    private DataInputStream input;
    private Socket socket;

    private boolean gameIsRunning = false;

    public Client(){

    }

    public static Client getInstance(){
        if(instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public void initConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            socket = new Socket(this.ip, this.port);
            this.output = new DataOutputStream(socket.getOutputStream());
            this.input = new DataInputStream(socket.getInputStream());

            ClientSend.SendPlayername();

            int length;
            int type;
            this.gameIsRunning = true;

            while (this.gameIsRunning) {
                System.out.println("waiting for message");
                Packet packet;
                length = getMessageLength();
                type = getMessageLength();
                packet = new Packet(getMessage(length-4));
                handleMessages(type, packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleMessages(int type, Packet packet){
        switch (type){
            case 1: //Login to server with playername and skills
                System.out.println("Type 1: Shouldn't be received. Only used to tell the server own playerinformation");
                break;
            case 2: //Server sends gamemode
                try {
                    ClientHandle.HandleGamemode(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int gameMode = 1;
                System.out.println("the picked gamemode is: " + gameMode);
                break;
            case 3: //Server sends playerturns and ids
                break;
            case 4: //Server sends all playerinformation
                break;
            case 5: //Server sends initial Gamefield
                break;
            case 6: //Server sends a moverequest
                break;
            case 7: //Client sends movereply
                System.out.println("Type 7: Shouldn't be received. Only used to tell the server own move");
                break;
            case 8: //Server sends a movereply of player in turn to ALL players
                break;
            case 9: //Server sends the new gamestate after calculating a move
                break;
            case 10: //Server sends an error. Could be illegal moves or other
                break;
            case 11: //Server sends players that the game is over and the winners' id
                break;
        }
    }

    public void SendPacket(Packet packet){
        try{
            output.write(packet.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getMessage(int length){
        byte[] message=new byte[length];
        try {
            this.input.read(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    private int getMessageLength(){
        byte[] buffer = new byte[4];
        try {
            this.input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Packet.convertToInt(buffer);
    }
}
