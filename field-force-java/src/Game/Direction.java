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

    public static Direction fromInt(int id){
        switch(id){
            case 1:
                return Direction.NORTH;
            case 2:
                return Direction.EAST;
            case 3:
                return Direction.SOUTH;
            case 4:
                return Direction.WEST;
        }
        return null;
    }
}
