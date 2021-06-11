package Game;

public enum SkillType {
    MOVEMENT(0),
    REGENERATE(1),
    FIRE(2),
    ROCKET(3),
    PUSH(4),
    BREAK(5);

    private final int id;

    SkillType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static SkillType fromInt(int id){
        return switch (id) {
            case 0 -> MOVEMENT;
            case 1 -> REGENERATE;
            case 2 -> FIRE;
            case 3 -> ROCKET;
            case 4 -> PUSH;
            case 5 -> BREAK;
            default -> null;
        };
    }
}
