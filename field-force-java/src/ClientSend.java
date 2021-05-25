import java.nio.ByteBuffer;

public class ClientSend {

    private static void SendPacket(Packet packet){
        packet.writeLength();
        Client.getInstance().SendPacket(packet);
    }

    public static void SendPlayername(){
        Packet packet = new Packet();
        packet.write(ClientPackets.PLAYERNAME.getId());
        packet.write("playername");
        SendPacket(packet);
    }

    public static void SendMove(){
        Packet packet = new Packet();
        packet.write(ClientPackets.PLAYERNAME.getId());
        packet.write("playername");
        SendPacket(packet);
    }
}
