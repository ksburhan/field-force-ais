package Game;

import Game.MapObject;

public class Player extends MapObject {
    public String name;
    public int hp;
    public int shield;

    public Player(String name){
        this.name = name;
        this.hp = 150;
        this.shield = 0;
    }
}
