package Connection;

public enum ServerPackets {
    GAMEMODE(2),
    PLAYERINFORMATION(3),
    GAMEFIELD(4),
    MOVEREQUEST(5),
    MOVEDISTRIBUTION(7),
    NEWGAMESTATE(8),
    ERROR(9),
    GAMEOVER(10);

    private final int id;

    ServerPackets(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
