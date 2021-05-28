package Connection;

import Connection.Client;
import Connection.ClientPackets;
import Connection.Packet;

public class ClientSend {

    private static void sendPacket(Packet packet){
        packet.writeLength();
        Client.getInstance().sendPacket(packet);
    }

    public static void sendPlayername(){
        Packet packet = new Packet();
        packet.write(ClientPackets.PLAYERNAME.getId());
        packet.write("playername");
        sendPacket(packet);
    }

    public static void sendMovereply(){
        Packet packet = new Packet();
        packet.write(ClientPackets.MOVEREPLY.getId());
        packet.write("playername");
        sendPacket(packet);
    }
}
