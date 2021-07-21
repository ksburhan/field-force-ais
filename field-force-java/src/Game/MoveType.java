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

    public static MoveType fromInt(int id){
        return switch (id) {
            case 1 -> MoveType.MOVEMENT;
            case 2 -> MoveType.ATTACK;
            case 3 -> MoveType.SKILL;
            default -> null;
        };
    }
}
