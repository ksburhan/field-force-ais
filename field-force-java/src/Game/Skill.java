package Game;

import AI.AI;
import Board.*;
import Connection.ServerPackets;

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
        this.cooldownLeft = skill.cooldownLeft;
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

    public void useSkill(Player player, Direction direction, GameState gameState){
        switch (this.type) {
            case MOVEMENT -> movementType(player, this, direction, gameState);
            case REGENERATE -> regenerateType(player, this, direction, gameState);
            case FIRE -> fireType(player, this, direction, gameState);
            case ROCKET -> rocketType(player, this, direction, gameState);
            case PUSH -> pushType(player, this, direction, gameState);
            case BREAK -> breakType(player, this, direction, gameState);
        }
        setOnCooldown();
    }

    public static void movementType(Player player, Skill skill, Direction direction, GameState gameState){
        for (int i = 0; i < skill.range; i++){
            Tile tile = gameState.getCurrentField().getField()[player.getxPos()][player.getyPos()];
            if(direction == Direction.NORTH){
                if (tile.getnTile() != null){
                    gameState.moveToTile(player, player.getxPos(), player.getyPos() - 1);
                }
                else {
                    return;
                }
            } else if(direction == Direction.EAST){
                if (tile.geteTile() != null){
                    gameState.moveToTile(player, player.getxPos() + 1, player.getyPos());
                }
                else {
                    return;
                }
            } else if(direction == Direction.SOUTH){
                if (tile.getsTile() != null){
                    gameState.moveToTile(player, player.getxPos(), player.getyPos() + 1);
                }
                else {
                    return;
                }
            } else if(direction == Direction.WEST){
                if (tile.getwTile() != null){
                    gameState.moveToTile(player, player.getxPos() - 1, player.getyPos());
                }
                else {
                    return;
                }
            }
        }
    }

    public static void regenerateType(Player player, Skill skill, Direction direction, GameState gameState){
        player.heal(skill.value);
    }

    public static void fireType(Player player, Skill skill, Direction direction, GameState gameState){
        int xTarget = player.getxPos();
        int yTarget = player.getyPos();
        Tile tile;
        for (int i = 0; i < skill.range; i++){
            tile = gameState.getCurrentField().getField()[xTarget][yTarget];
            if(direction == Direction.NORTH){
                if (tile.getnTile() != null){
                    yTarget -= i;
                }
                else {
                    return;
                }
            } else if(direction == Direction.EAST){
                if (tile.geteTile() != null){
                    xTarget += i;
                }
                else {
                    return;
                }
            } else if(direction == Direction.SOUTH){
                if (tile.getsTile() != null){
                    yTarget += i;
                }
                else {
                    return;
                }
            } else if(direction == Direction.WEST){
                if (tile.getwTile() != null){
                    xTarget -= i;
                }
                else {
                    return;
                }
            }
            MapObject targetCellContent = gameState.getCurrentField().getField()[xTarget][yTarget].getContent();
            if (targetCellContent.getId() == '0' || targetCellContent instanceof Fire || targetCellContent instanceof Consumable)
            {
                Fire fire = new Fire('f', xTarget, yTarget);
                gameState.getCurrentField().getFieldChars()[xTarget][yTarget] = fire.getId();
                gameState.getCurrentField().getField()[xTarget][yTarget].setContent(fire);
            }
            else if (targetCellContent instanceof Player)
            {
                ((Player) targetCellContent).takeDamage(skill.value);
            }
            else if (targetCellContent instanceof Wall)
            {
                return;
            }
        }
    }

    public static void rocketType(Player player, Skill skill, Direction direction, GameState gameState){
        List<int[]> targets = new ArrayList<>();
        int[] target = new int[2];
        if(direction == Direction.NORTH){
            target[0] = player.getxPos();
            target[1] = player.getyPos() - skill.range;
        } else if(direction == Direction.EAST){
            target[0] = player.getxPos() + skill.range;
            target[1] = player.getyPos();
        } else if(direction == Direction.SOUTH){
            target[0] = player.getxPos();
            target[1] = player.getyPos() + skill.range;
        } else if(direction == Direction.WEST){
            target[0] = player.getxPos() - skill.range;
            target[1] = player.getyPos();
        }
        targets.add(target);
        if (direction == Direction.NORTH || direction == Direction.SOUTH)
        {
            targets.add(new int[]{target[0] + 1, target[1]});
            targets.add(new int[]{target[0] - 1, target[1]});
        }
        else if (direction == Direction.EAST || direction == Direction.WEST)
        {
            targets.add(new int[]{target[0], target[1] + 1});
            targets.add(new int[]{target[0], target[1] - 1});
        }
        for (int[] t : targets) {
            if(t[0] >= 0 && t[0] < gameState.getCurrentField().getDimension() &&
                    t[1] >= 0 && t[1] < gameState.getCurrentField().getDimension()) {
                MapObject targetCellContent = gameState.getCurrentField().getField()[t[0]][t[1]].getContent();
                if (targetCellContent.getId() == '0' || targetCellContent instanceof Fire || targetCellContent instanceof Consumable)
                {
                    Fire fire = new Fire('f', t[0], t[1]);
                    gameState.getCurrentField().getFieldChars()[t[0]][t[1]] = fire.getId();
                    gameState.getCurrentField().getField()[t[0]][t[1]].setContent(fire);
                }
                else if (targetCellContent instanceof Player)
                {
                    ((Player) targetCellContent).takeDamage(skill.value);
                }
            }
        }
    }

    public static void pushType(Player player, Skill skill, Direction direction, GameState gameState){
        int xTarget = 0;
        int yTarget = 0;
        if(direction == Direction.NORTH){
            xTarget = player.getxPos();
            yTarget = player.getyPos() - skill.range;
        } else if(direction == Direction.EAST){
            xTarget = player.getxPos() + skill.range;
            yTarget = player.getyPos();
        } else if(direction == Direction.SOUTH){
            xTarget = player.getxPos();
            yTarget = player.getyPos() + skill.range;
        } else if(direction == Direction.WEST){
            xTarget = player.getxPos() - skill.range;
            yTarget = player.getyPos();
        }
        if(xTarget >= 0 && xTarget < gameState.getCurrentField().getDimension() &&
                yTarget >= 0 && yTarget < gameState.getCurrentField().getDimension()) {
            Tile tile = gameState.getCurrentField().getField()[xTarget][yTarget];
            if(tile.getnTile() != null){
                Tile nTile = tile.getnTile();
                MapObject targetCellContent = nTile.getContent();
                if(targetCellContent instanceof Player && nTile.getnTile() != null){
                    gameState.moveToTile((Player) targetCellContent, targetCellContent.getxPos(), targetCellContent.getyPos() - 1);
                }
            }
            if(tile.geteTile() != null){
                Tile eTile = tile.geteTile();
                MapObject targetCellContent = eTile.getContent();
                if(targetCellContent instanceof Player && eTile.geteTile() != null){
                    gameState.moveToTile((Player) targetCellContent, targetCellContent.getxPos() + 1, targetCellContent.getyPos());
                }
            }
            if(tile.getsTile() != null){
                Tile sTile = tile.getsTile();
                MapObject targetCellContent = sTile.getContent();
                if(targetCellContent instanceof Player && sTile.getsTile() != null){
                    gameState.moveToTile((Player) targetCellContent, targetCellContent.getxPos(), targetCellContent.getyPos() + 1);
                }
            }
            if(tile.getwTile() != null){
                Tile wTile = tile.getwTile();
                MapObject targetCellContent = wTile.getContent();
                if(targetCellContent instanceof Player && wTile.getwTile() != null){
                    gameState.moveToTile((Player) targetCellContent, targetCellContent.getxPos() - 1, targetCellContent.getyPos());
                }
            }
        }
    }

    public static void breakType(Player player, Skill skill, Direction direction, GameState gameState){
        int xTarget = player.getxPos();
        int yTarget = player.getyPos();
        Tile tile;
        boolean broke = false;
        for (int i = 0; i < skill.range; i++){
            tile = gameState.getCurrentField().getField()[xTarget][yTarget];
            if(direction == Direction.NORTH){
                if (tile.getnTile() != null){
                    yTarget -= 1;
                }
                else {
                    return;
                }
            } else if(direction == Direction.EAST){
                if (tile.geteTile() != null){
                    xTarget += 1;
                }
                else {
                    return;
                }
            } else if(direction == Direction.SOUTH){
                if (tile.getsTile() != null){
                    yTarget += 1;
                }
                else {
                    return;
                }
            } else if(direction == Direction.WEST){
                if (tile.getwTile() != null){
                    xTarget -= 1;
                }
                else {
                    return;
                }
            }
            MapObject targetCellContent = gameState.getCurrentField().getField()[xTarget][yTarget].getContent();
            if (targetCellContent instanceof Player)
            {
                ((Player) targetCellContent).takeDamage(skill.value);
            }
            else if (targetCellContent instanceof Wall)
            {
                if(broke)
                    return;
                ((Wall) targetCellContent).takeDamage(GameConstants.WALL_HP);
                broke = true;
            }
        }
    }

    public void setOnCooldown(){
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
