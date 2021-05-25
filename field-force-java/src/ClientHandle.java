public class ClientHandle {

    public static void HandleGamemode(Packet packet) throws Exception {
        int gamemode = packet.readInt();
        String message = packet.readString();
        System.out.println("CLIENTHANDLE " + gamemode + " " + message);
    }

}
