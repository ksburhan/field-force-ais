package Board;

import java.util.ArrayList;
import java.util.List;

public class Consumable extends MapObject {
    public static List<Consumable> allConsumables = new ArrayList<Consumable>();

    private String conName;
    private int healing;
    private int shield;

    public Consumable(char id, int x, int y) {
        super(id, x, y);
        Consumable c = getConsumable(id);
        assert c != null;
        this.conName = c.conName;
        this.healing = c.healing;
        this.shield = c.shield;
    }

    public Consumable(char id, String conName ,int healing, int shield) {
        super(id);
        this.conName = conName;
        this.healing = healing;
        this.shield = shield;
    }

    public static Consumable getConsumable(char id)
    {
        for(Consumable s : allConsumables)
        {
            if (s.id == id)
                return s;
        }
        return null;
    }

    public String getConName() {
        return conName;
    }

    public void setConName(String conName) {
        this.conName = conName;
    }

    public int getHealing() {
        return healing;
    }

    public void setHealing(int healing) {
        this.healing = healing;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }
}
