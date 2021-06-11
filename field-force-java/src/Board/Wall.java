package Board;

import AI.AI;
import Game.GameConstants;

public class Wall extends MapObject {
    private int hp = GameConstants.WALL_HP;

    public Wall(char id, int x, int y){
        super(id, x, y);
    }

    public Wall(char id, int x, int y, int hp){
        super(id, x, y);
        this.hp = hp;
    }

    public void takeDamage(int damage)
    {
        hp -= damage;
        if (hp <= 0)
        {
            destroy();
        }
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
