package Connection;

import AI.AI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.lang.System.exit;

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

            ClientSend.sendPlayername();

            int length;
            int type;
            this.gameIsRunning = true;

            while (this.gameIsRunning) {
                Packet packet;
                length = getMessageLength();
                type = getMessageLength();
                packet = new Packet(getMessage(length-4));
                handleMessages(type, packet);
            }
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleMessages(int type, Packet packet){
        ServerPackets typeEnum = ServerPackets.fromInt(type);
        switch (typeEnum){
            case GAMEMODE: //Server sends gamemode and id
                try {
                    System.out.println("Type 2");
                    ClientHandle.handleGamemode(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                    exit(-2);
                }
                break;
            case PLAYERINFORMATION: //Server sends playerturns and playerinformation (with ids for turnorder and skills)
                try {
                    System.out.println("Type 3");
                    ClientHandle.handlePlayerinformation(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                    exit(-3);
                }
                break;
            case GAMEFIELD: //Server sends initial Gamefield
                try {
                    System.out.println("Type 4");
                    ClientHandle.handleInitialMap(packet);
                    AI.getInstance().getCurrentState().getCurrentField().printMap();
                } catch (Exception e) {
                    e.printStackTrace();
                    exit(-4);
                }
                break;
            case MOVEREQUEST: //Server sends a moverequest
                try {
                    System.out.println("Type 5");
                    ClientHandle.handleMoveRequest(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                    exit(-5);
                }
                break;
            case NEWGAMESTATE: //Server sends the new gamestate after calculating a move
                try {
                    System.out.println("Type 7");
                    ClientHandle.handleNewGamestate(packet);
                    AI.getInstance().getCurrentState().getCurrentField().printMap();
                } catch (Exception e) {
                    e.printStackTrace();
                    exit(-8);
                }
                break;
            case MOVEDISTRIBUTION: //Server sends a movereply of player in turn to ALL players
                try {
                    System.out.println("Type 8");
                    ClientHandle.handleMovedistribution(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                    exit(-7);
                }
                break;
            case ERROR: //Server sends an error. Could be illegal moves
                try {
                    System.out.println("Type 9");
                    ClientHandle.handleErrors(packet);
                    exit(-9);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case GAMEOVER: //Server sends players that the game is over and the winners' id
                try {
                    System.out.println("Type 10");
                    ClientHandle.handleGameover(packet);
                    this.gameIsRunning = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Error: No type detected: " + type);
                break;
        }
    }

    public void sendPacket(Packet packet){
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
