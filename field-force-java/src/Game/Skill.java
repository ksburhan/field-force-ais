package Game;

public class Skill {

    private int id;
    private String name;
    private int cooldown;
    private int cooldownLeft = 0;
    private int range;

    public Skill(int id, String name, int cooldown, int range) {
        this.id = id;
        this.name = name;
        this.cooldown = cooldown;
        this.range = range;
    }
    public Skill(int id) {
        this.id = id;
    }

    public void prepareForNextRound()
    {
        if(cooldownLeft > 0)
        {
            cooldownLeft--;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCooldownLeft() {
        return cooldownLeft;
    }

    public void setCooldownLeft(int cooldownLeft) {
        this.cooldownLeft = cooldownLeft;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
}
