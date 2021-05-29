package Game;

public enum MoveType {
    MOVEMENT(1),
    ATTACK(2),
    SKILL(3);

    private final int id;

    MoveType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
