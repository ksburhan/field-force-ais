package Game;

public class Move {

    private MoveType type;
    private Direction direction;
    private Skill skill;

    public Move(MoveType type, Direction direction, Skill skill){
        this.type = type;
        this.direction = direction;
        this.skill = skill;
    }

    public MoveType getType() {
        return type;
    }

    public void setType(MoveType type) {
        this.type = type;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}
