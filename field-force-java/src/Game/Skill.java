package Game;

import java.util.ArrayList;
import java.util.List;

public class Skill {

    public static List<Skill> allSkills = new ArrayList<Skill>();

    private int id;
    private String name;
    private int cooldown;
    private int range;
    private int value;
    private SkillType type;

    private int cooldownLeft = 0;

    public Skill(int id, String name, int cooldown, int range, int value, SkillType type) {
        this.id = id;
        this.name = name;
        this.cooldown = cooldown;
        this.range = range;
        this.value = value;
        this.type = type;
    }
    public Skill(int id) {
        this.id = id;
    }

    public static Skill getSkill(int id)
    {
        for(Skill s : allSkills)
        {
            if (s.id == id)
                return s;
        }
        return null;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
