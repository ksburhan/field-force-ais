public class ClientHandle {

    public static void HandleGamemode(Packet packet) throws Exception {
        int gamemode = packet.readInt();
        String message = packet.readString();
    }

    public static void HandleTurnorder(Packet packet) throws Exception {
        int gamemode = packet.readInt();
        String message = packet.readString();
        System.out.println("CLIENTHANDLE " + gamemode + " " + message);
    }

    public static void HandlePlayerinformation(Packet packet) throws Exception {
        int gamemode = packet.readInt();
        String message = packet.readString();
        System.out.println("CLIENTHANDLE " + gamemode + " " + message);
    }

    public static void HandleGamefield(Packet packet) throws Exception {
        int gamemode = packet.readInt();
        String message = packet.readString();
        System.out.println("CLIENTHANDLE " + gamemode + " " + message);
    }

    public static void HandleMoverequest(Packet packet) throws Exception {
        int gamemode = packet.readInt();
        String message = packet.readString();
        System.out.println("CLIENTHANDLE " + gamemode + " " + message);
    }

    public static void HandleMovedistribution(Packet packet) throws Exception {
        int gamemode = packet.readInt();
        String message = packet.readString();
        System.out.println("CLIENTHANDLE " + gamemode + " " + message);
    }

    public static void HandleNewGamestate(Packet packet) throws Exception {
        int gamemode = packet.readInt();
        String message = packet.readString();
        System.out.println("CLIENTHANDLE " + gamemode + " " + message);
    }

    public static void HandleErrors(Packet packet) throws Exception {
        int gamemode = packet.readInt();
        String message = packet.readString();
        System.out.println("CLIENTHANDLE " + gamemode + " " + message);
    }

    public static void HandleGameover(Packet packet) throws Exception {
        int gamemode = packet.readInt();
        String message = packet.readString();
        System.out.println("CLIENTHANDLE " + gamemode + " " + message);
    }
}
