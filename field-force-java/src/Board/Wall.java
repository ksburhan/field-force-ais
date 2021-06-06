package Board;

public class Wall extends MapObject {
    private int hp = 20;

    public Wall(char id, int x, int y){
        super(id, x, y);
    }

    public Wall(char id, int x, int y, int hp){
        super(id, x, y);
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
