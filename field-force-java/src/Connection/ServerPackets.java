package Connection;

public enum ServerPackets {
    GAMEMODE(2),
    PLAYERINFORMATION(3),
    GAMEFIELD(4),
    MOVEREQUEST(5),
    NEWGAMESTATE(7),
    MOVEDISTRIBUTION(8),
    ERROR(9),
    GAMEOVER(10);

    private final int id;

    ServerPackets(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ServerPackets fromInt(int id){
        return switch (id) {
            case 2 -> GAMEMODE;
            case 3 -> PLAYERINFORMATION;
            case 4 -> GAMEFIELD;
            case 5 -> MOVEREQUEST;
            case 7 -> NEWGAMESTATE;
            case 8 -> MOVEDISTRIBUTION;
            case 9 -> ERROR;
            case 10 -> GAMEOVER;
            default -> null;
        };
    }
}
