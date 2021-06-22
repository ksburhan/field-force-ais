package Game;

import AI.AI;
import Board.MapObject;

public class Player extends MapObject {
    private int playerNumber;
    private String name;
    private int hp;
    private int shield;

    public boolean active = true;
    public int onFire = 0;

    private Skill skill1;
    private Skill skill2;

    public Player(char id, int playerNumber, String name, int x, int y){
        super(id, x, y);
        this.id = id;
        this.playerNumber = playerNumber;
        this.name = name;
        this.hp = GameConstants.HP;
        this.shield = GameConstants.SHIELD;
    }

    public Player(char id, int playerNumber, String name, int x, int y, Skill skill1, Skill skill2){
        super(id, x, y);
        this.id = id;
        this.playerNumber = playerNumber;
        this.name = name;
        this.hp = GameConstants.HP;
        this.shield = GameConstants.SHIELD;
        this.skill1 = skill1;
        this.skill2 = skill2;
    }

    public Player(Player other){
        super(other);
        this.id = other.id;
        this.playerNumber = other.playerNumber;
        this.name = other.name;
        this.hp = other.hp;
        this.shield = other.shield;
        this.active = other.active;
        this.onFire = other.onFire;
        this.skill1 = new Skill(other.skill1);
        this.skill2 = new Skill(other.skill2);
    }

    public void takeDamage(int damage){
        if(shield > 0)
        {
            takeShieldDamage(damage);
        }
        else
        {
            hp -= damage;
            if (hp <= 0)
                setInactive();
        }
    }
    public void takeShieldDamage(int shieldDamage) {
        shield -= shieldDamage;
        if (shield <= 0)
        {
            int damage = shield * (-1);
            shield = 0;
            takeDamage(damage);
        }
    }
    public void heal(int heal)
    {
        hp += heal;
        if (hp > GameConstants.HP)
            hp = GameConstants.HP;
    }
    public void chargeShield(int charge)
    {
        shield += charge;
        if (shield > GameConstants.SHIELD)
            shield = GameConstants.SHIELD;
    }

    public void setOnFire()
    {
        onFire = GameConstants.ON_FIRE_EFFECT_DURATION;
    }

    public void setInactive()
    {
        destroy();
        active = false;
    }

    public void prepareForNextRound()
    {
        if(onFire > 0)
        {
            takeDamage(GameConstants.ON_FIRE_DAMAGE);
            onFire--;
        }
        if(skill1 != null)
            skill1.prepareForNextRound();
        if(skill2 != null)
            skill2.prepareForNextRound();
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public Skill getSkill1() {
        return skill1;
    }

    public void setSkill1(Skill skill1) {
        this.skill1 = skill1;
    }

    public Skill getSkill2() {
        return skill2;
    }

    public void setSkill2(Skill skill2) {
        this.skill2 = skill2;
    }
}
