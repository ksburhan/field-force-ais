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
        switch(id){
            case 1:
                return MoveType.MOVEMENT;
            case 2:
                return MoveType.ATTACK;
            case 3:
                return MoveType.SKILL;
        }
        return null;
    }
}
