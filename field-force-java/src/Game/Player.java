package Game;

import AI.AI;
import Board.MapObject;

import java.util.ArrayList;
import java.util.List;

public class Player extends MapObject {

    public static List<Player> ALL_PLAYERS = new ArrayList<>();

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
        this.shield = 0;
    }

    public Player(char id, int playerNumber, String name, int hp, int shield, int x, int y, Skill skill1, Skill skill2){
        super(id, x, y);
        this.id = id;
        this.playerNumber = playerNumber;
        this.name = name;
        this.hp = hp;
        this.shield = shield;
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

    /**
     * @param damage
     * @param gameState
     * if shield is available, shielddamage is taken first. if damage is still left reduce hp
     */
    public void takeDamage(int damage, GameState gameState){
        if(shield > 0)
        {
            takeShieldDamage(damage, gameState);
        }
        else
        {
            hp -= damage;
            if (hp <= 0)
                setInactive(gameState);
        }
    }

    /**
     * @param shieldDamage
     * @param gameState
     * reduces shield damage, if damage is still left call takeDamage function with rest
     */
    public void takeShieldDamage(int shieldDamage, GameState gameState) {
        shield -= shieldDamage;
        if (shield <= 0)
        {
            int damage = shield * (-1);
            shield = 0;
            takeDamage(damage, gameState);
        }
    }

    /**
     * @param heal
     * heal player by value
     */
    public void heal(int heal)
    {
        hp += heal;
        if (hp > GameConstants.HP)
            hp = GameConstants.HP;
    }

    /**
     * @param charge
     * charge shield by value
     */
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

    public void setInactive(GameState gameState)
    {
        destroy(gameState);
		hp = 0;
		shield = 0;
        active = false;
    }

    public void prepareForNextRound(GameState gameState)
    {
        if(onFire > 0)
        {
            takeDamage(GameConstants.ON_FIRE_DAMAGE, gameState);
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
