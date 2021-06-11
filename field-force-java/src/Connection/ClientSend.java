package Connection;

import AI.AI;
import Game.Move;

public class ClientSend {

    private static void sendPacket(Packet packet){
        packet.writeLength();
        Client.getInstance().sendPacket(packet);
    }

    public static void sendPlayername(){
        Packet packet = new Packet();
        packet.write(ClientPackets.PLAYERNAME.getId());
        packet.write(AI.playername);
        sendPacket(packet);
    }

    public static void sendMovereply(int id, Move move){
        Packet packet = new Packet();
        packet.write(ClientPackets.MOVEREPLY.getId());
        packet.write(id);
        packet.write(move);
        sendPacket(packet);
    }
}
