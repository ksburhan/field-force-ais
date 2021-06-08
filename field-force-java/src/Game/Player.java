package Game;

import AI.AI;
import Board.MapObject;

public class Player extends MapObject {
    private int playerNumber;
    private String name;
    private int hp = 150;
    private int shield = 0;

    public boolean active = true;
    public int onFire = 0;

    private Skill skill1;
    private Skill skill2;

    public Player(char id, int playerNumber, String name, int x, int y){
        super(id, x, y);
        this.id = id;
        this.playerNumber = playerNumber;
        this.name = name;
    }

    public void TakeDamage(int damage){
        if(shield > 0)
        {
            TakeShieldDamage(damage);
        }
        else
        {
            hp -= damage;
            if (hp <= 0)
                SetInactive();
        }
    }
    public void TakeShieldDamage(int shieldDamage) {
        shield -= shieldDamage;
        if (shield <= 0)
        {
            int damage = shield * (-1);
            shield = 0;
            TakeDamage(damage);
        }
    }
    public void Heal(int heal)
    {
        hp += heal;
        if (hp > GameConstants.HP)
            hp = GameConstants.HP;
    }
    public void ChargeShield(int charge)
    {
        shield += charge;
        if (shield > GameConstants.SHIELD)
            shield = GameConstants.SHIELD;
    }

    public void SetOnFire()
    {
        onFire = GameConstants.ON_FIRE_EFFECT_DURATION;
    }

    public void SetInactive()
    {
        Destroy();
        active = false;
    }

    public void PrepareForNextRound()
    {
        if(onFire > 0)
        {
            TakeDamage(GameConstants.ON_FIRE_DAMAGE);
            onFire--;
        }
        if(skill1 != null)
            skill1.PrepareForNextRound();
        if(skill2 != null)
            skill2.PrepareForNextRound();
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
