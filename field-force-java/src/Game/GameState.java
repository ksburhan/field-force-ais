package Game;

import AI.AI;
import Board.*;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private static List<Player> players = new ArrayList<>();
    private List<Integer> playerInTurn = new ArrayList<>();

    private List<Player> currentPlayers = new ArrayList<>();

    private List<Fire> fires = new ArrayList<>();
    private List<Wall> walls = new ArrayList<>();
    private List<Consumable> consumables = new ArrayList<>();

    private GameField currentField;
    private Move lastMove;

    public GameState(GameField gameField, List<Integer> playerInTurn){
        this.currentField = gameField;
        this.playerInTurn = playerInTurn;
    }

    public GameState(GameState other){
        this.playerInTurn = new ArrayList<>(other.playerInTurn);
        this.currentPlayers = new ArrayList<>(other.currentPlayers);
        this.fires = new ArrayList<>(other.fires);
        this.walls = new ArrayList<>(other.walls);
        this.consumables = new ArrayList<>(other.consumables);

        this.currentField = new GameField(other.currentField);
        this.lastMove = new Move(other.lastMove);
    }

    public GameState(GameField gameField, List<Integer> playerInTurn, List<Fire> fires, List<Wall> walls, List<Consumable> consumables){
        this.currentField = gameField;
        this.currentPlayers = GameState.players;
        this.playerInTurn = playerInTurn;
        this.fires = fires;
        this.walls = walls;
        this.consumables = consumables;
    }

    public GameState(GameField gameField, List<Player> players, List<Integer> playerInTurn, List<Fire> fires, List<Wall> walls, List<Consumable> consumables){
        this.currentField = gameField;
        this.currentPlayers = players;
        this.playerInTurn = playerInTurn;
        this.fires = fires;
        this.walls = walls;
        this.consumables = consumables;
    }

    public List<Move> getAllMoves(int playerID){
        List<Move> moves = new ArrayList<>();
        Player player = currentPlayers.get(playerID-1);
        Tile tile = currentField.getField()[player.getxPos()][player.getyPos()];
        for (MoveType t : MoveType.values()){
            switch (t.getId()){
                case 1: // MOVEMENT
                    if(tile.getnTile() != null)
                        moves.add(new Move(MoveType.MOVEMENT, Direction.NORTH, null));
                    if(tile.geteTile() != null)
                        moves.add(new Move(MoveType.MOVEMENT, Direction.EAST, null));
                    if(tile.getsTile() != null)
                        moves.add(new Move(MoveType.MOVEMENT, Direction.SOUTH, null));
                    if(tile.getwTile() != null)
                        moves.add(new Move(MoveType.MOVEMENT, Direction.WEST, null));
                    break;
                case 2: // ATTACK
                    if(tile.getnTile() != null && isValidTarget(tile.getnTile().getContent().id))
                        moves.add(new Move(MoveType.ATTACK, Direction.NORTH, null));
                    if(tile.geteTile() != null && isValidTarget(tile.geteTile().getContent().id))
                        moves.add(new Move(MoveType.ATTACK, Direction.EAST, null));
                    if(tile.getsTile() != null && isValidTarget(tile.getsTile().getContent().id))
                        moves.add(new Move(MoveType.ATTACK, Direction.SOUTH, null));
                    if(tile.getwTile() != null && isValidTarget(tile.getwTile().getContent().id))
                        moves.add(new Move(MoveType.ATTACK, Direction.WEST, null));
                    break;
                case 3: // SKILL
                    for (Direction d : Direction.values()){
                        if(player.getSkill1() != null && player.getSkill1().getCooldownLeft() == 0)
                            moves.add(new Move(MoveType.SKILL, d, player.getSkill1()));
                        if(player.getSkill2() != null && player.getSkill2().getCooldownLeft() == 0)
                            moves.add(new Move(MoveType.SKILL, d, player.getSkill2()));
                    }
                    break;
            }
        }
        return moves;
    }

    private boolean isValidTarget(char c){
        for (char x : GameConstants.VALID_TARGETS){
            if(x == c){
                return true;
            }
        }
        return false;
    }

    public void simulateNextGamestate(int playerID, Move move){
        Player player = currentPlayers.get(playerID-1);
        switch (move.getType().getId()) {
            // MOVEMENT
            case 1 -> {
                if (move.getDirection() == Direction.NORTH)
                    moveToTile(player, player.getxPos(), player.getyPos() - 1);
                if (move.getDirection() == Direction.EAST)
                    moveToTile(player, player.getxPos() + 1, player.getyPos());
                if (move.getDirection() == Direction.SOUTH)
                    moveToTile(player, player.getxPos(), player.getyPos() + 1);
                if (move.getDirection() == Direction.WEST)
                    moveToTile(player, player.getxPos() - 1, player.getyPos());
            }
            // ATTACK
            case 2 -> {
                if (move.getDirection() == Direction.NORTH)
                    attackTile(player, player.getxPos(), player.getyPos() - 1);
                if (move.getDirection() == Direction.EAST)
                    attackTile(player, player.getxPos() + 1, player.getyPos());
                if (move.getDirection() == Direction.SOUTH)
                    attackTile(player, player.getxPos(), player.getyPos() + 1);
                if (move.getDirection() == Direction.WEST)
                    attackTile(player, player.getxPos() - 1, player.getyPos());
            }
            // SKILL
            case 3 -> {
                Skill skill = player.getSkill1();
                if (move.getSkill().getId() == player.getSkill2().getId())
                    skill = player.getSkill2();
                skill.useSkill(player, move.getDirection(), this);
            }
        }
        if(playerInTurn.remove(Integer.valueOf(playerID)))
            playerInTurn.add(playerID);
        prepareForNextRound();
        this.lastMove = move;
    }

    public void moveToTile(Player player, int xTarget, int yTarget){
        MapObject targetCellContent = currentField.getField()[xTarget][yTarget].getContent();
        int x = player.getxPos();
        int y = player.getyPos();
        if (targetCellContent.getId() == '0')
        {
            AI.instance.getCurrentState().getCurrentField().getFieldChars()[x][y] = '0';
            AI.instance.getCurrentState().getCurrentField().getField()[x][y].setContent(new MapObject('0', x, y));
            AI.instance.getCurrentState().getCurrentField().getFieldChars()[xTarget][yTarget] = player.id;
            AI.instance.getCurrentState().getCurrentField().getField()[xTarget][yTarget].setContent(player);
            player.setPos(xTarget, yTarget);
        }
        else if (targetCellContent instanceof Player)
        {
            player.takeDamage(GameConstants.WALK_IN_PLAYER_DAMAGE);
            ((Player) targetCellContent).takeDamage(GameConstants.PLAYER_WALKED_INTO_DAMAGE);
        }
        else if (targetCellContent instanceof Fire)
        {
            AI.instance.getCurrentState().getCurrentField().getFieldChars()[x][y] = '0';
            AI.instance.getCurrentState().getCurrentField().getField()[x][y].setContent(new MapObject('0', x, y));
            AI.instance.getCurrentState().getCurrentField().getFieldChars()[xTarget][yTarget] = player.id;
            AI.instance.getCurrentState().getCurrentField().getField()[x][y].setContent(player);
            player.setPos(xTarget, yTarget);
            player.takeDamage(GameConstants.ON_FIRE_DAMAGE);
            player.setOnFire();
        }
        else if (targetCellContent instanceof Wall)
        {
            player.takeDamage(GameConstants.WALK_IN_WALL_DAMAGE);
            ((Wall) targetCellContent).takeDamage(GameConstants.WALL_TAKE_DAMAGE);
        }
        else if (targetCellContent.id == 'x')
        {
            AI.instance.getCurrentState().getCurrentField().getFieldChars()[x][y] = '0';
            AI.instance.getCurrentState().getCurrentField().getField()[x][y].setContent(new MapObject('0', x, y));
            player.setInactive();
        }
        else if (targetCellContent instanceof Consumable)
        {
            AI.instance.getCurrentState().getCurrentField().getFieldChars()[x][y] = '0';
            AI.instance.getCurrentState().getCurrentField().getField()[x][y].setContent(new MapObject('0', x, y));
            AI.instance.getCurrentState().getCurrentField().getFieldChars()[xTarget][yTarget] = player.id;
            AI.instance.getCurrentState().getCurrentField().getField()[x][y].setContent(player);
            player.setPos(xTarget, yTarget);
            if(((Consumable) targetCellContent).getHealing() > 0)
                player.heal(((Consumable) targetCellContent).getHealing());
            else
                player.takeDamage(((Consumable) targetCellContent).getHealing());
            if(((Consumable) targetCellContent).getShield() > 0)
                player.chargeShield(((Consumable) targetCellContent).getShield());
        }
    }

    public void attackTile(Player player, int xTarget, int yTarget){
        MapObject targetCellContent = currentField.getField()[xTarget][yTarget].getContent();
        if (targetCellContent instanceof Player)
        {
            ((Player) targetCellContent).takeDamage(GameConstants.ATTACK_DAMAGE);
        }
        else if (targetCellContent instanceof Wall)
        {
            ((Wall) targetCellContent).takeDamage(GameConstants.ATTACK_DAMAGE);
        }
    }

    private void prepareForNextRound()
    {
        for (Player p : this.currentPlayers) {
            if (p.active) {
                p.prepareForNextRound();
            }
        }
        for (Fire f : fires)
        {
            if(f != null)
                f.prepareForNextRound();
        }
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static void setPlayers(List<Player> players) {
        GameState.players = players;
    }

    public List<Player> getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(List<Player> currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public List<Fire> getFires() {
        return fires;
    }

    public void setFires(List<Fire> fires) {
        this.fires = fires;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public void setWalls(List<Wall> walls) {
        this.walls = walls;
    }

    public List<Consumable> getConsumables() {
        return consumables;
    }

    public void setConsumables(List<Consumable> consumables) {
        this.consumables = consumables;
    }

    public List<Integer> getPlayerInTurn() {
        return playerInTurn;
    }

    public void setPlayerInTurn(List<Integer> playerInTurn) {
        this.playerInTurn = playerInTurn;
    }

    public GameField getCurrentField() {
        return currentField;
    }

    public void setCurrentField(GameField currentField) {
        this.currentField = currentField;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }
}
