package Board;

import AI.AI;
import Game.GameConstants;
import Game.GameState;

public class Wall extends MapObject {
    private int hp = GameConstants.WALL_HP;

    public Wall(char id, int x, int y){
        super(id, x, y);
    }

    public Wall(char id, int x, int y, int hp){
        super(id, x, y);
        this.hp = hp;
    }

    public Wall(Wall other){
        super(other);
        this.hp = other.hp;
    }

    public void takeDamage(int damage, GameState gameState)
    {
        hp -= damage;
        if (hp <= 0)
        {
            destroy(gameState);
        }
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
