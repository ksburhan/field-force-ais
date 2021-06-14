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
    public Skill(int id, int cooldownLeft) {
        this.id = id;
        Skill s = getSkill(id);
        assert s != null;
        this.name = s.getName();
        this.cooldown = s.getCooldown();
        this.range = s.getRange();
        this.value = s.getValue();
        this.type = s.getType();
        this.cooldownLeft = cooldownLeft;
    }
    public Skill(Skill skill) {
        this.id = skill.id;
        this.name = skill.name;
        this.cooldown = skill.cooldown;
        this.range = skill.range;
        this.value = skill.value;
        this.type = skill.type;
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

    public void setUsed(){
        this.cooldownLeft = cooldown;
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

    public SkillType getType() {
        return type;
    }

    public void setType(SkillType type) {
        this.type = type;
    }
}
