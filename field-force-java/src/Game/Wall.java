package Game;

import Game.MapObject;

public class Wall extends MapObject {
    private int hp;

    public Wall(char id, int x, int y){
        super(id, x, y);
        hp = 20;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
