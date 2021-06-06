package Connection;

public enum ClientPackets {
    PLAYERNAME(1),
    MOVEREPLY(6);

    private final int id;

    ClientPackets(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
