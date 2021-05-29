package Game;

public enum Direction {
    NORTH(1),
    EAST(2),
    SOUTH(3),
    WEST(4);

    private final int id;

    Direction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
