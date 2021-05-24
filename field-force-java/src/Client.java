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

            int type;
            byte[] message;
            this.gameIsRunning = true;

            while (this.gameIsRunning) {
                System.out.println("waiting for message");
                type = this.input.read();
                message = getMessage(getMessageLength());
                handleMessages(type, message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleMessages(int type,byte[] message){
        switch (type){
            case 1:
                System.out.println("Type 1: Shouldn't be received. Only used to tell the server playername");
                break;
            case 2:
                int gameMode = message[0];
                System.out.println("the picked gamemode is: " + gameMode);
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
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
        return convertToInt(buffer);
    }

    private byte[] splitMessage(byte[] message, int start, int end) {
        int size=end-start+1;
        byte[] splitted=new byte[size];
        int counter=0;
        for(int i=start;i<=end;i++){
            splitted [counter] = message[i];
            counter++;

        }
        return splitted;
    }

    private int convertToInt(byte[] buffer){
        int result=0;
        if(buffer.length==4) {
            result = buffer[0] << 24 | (buffer[1] & 0xFF) << 16 | (buffer[2] & 0xFF) << 8 | (buffer[3] & 0xFF);
        }
        else if (buffer.length==2){
            result=(buffer[0] & 0xFF) << 8 | (buffer[1] & 0xFF);
        }
        else{
            exit(-1);
        }
        return result;
    }
}
