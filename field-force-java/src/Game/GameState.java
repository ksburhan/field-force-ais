package Game;

import Board.Fire;
import Board.GameField;
import Board.Tile;
import Board.Wall;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private static List<Player> players = new ArrayList<>();
    private List<Integer> playerInTurn = new ArrayList<>();


    private List<Fire> fires = new ArrayList<>();
    private List<Wall> walls = new ArrayList<>();

    private GameField currentField;

    public GameState(GameField gameField, List<Integer> playerInTurn){
        this.currentField = gameField;
        this.playerInTurn = playerInTurn;
    }

    public GameState(GameField gameField, List<Player> players, List<Integer> playerInTurn, List<Fire> fires, List<Wall> walls){
        this.currentField = gameField;
        GameState.players = players;
        this.playerInTurn = playerInTurn;
        this.fires = fires;
        this.walls = walls;
    }

    public List<Move> getAllMoves(int playerID){
        List<Move> moves = new ArrayList<>();
        Player player = players.get(playerID-1);
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
                            moves.add(new Move(MoveType.SKILL, d, player.getSkill1()));
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
        Player player = players.get(playerID-1);
        switch (move.getType().getId())
        {
            case 1: // MOVEMENT
                if (move.getDirection() == Direction.NORTH)
                    MoveToTile(player, player.getxPos(), player.getyPos() - 1);
                if (move.getDirection() == Direction.EAST)
                    MoveToTile(player, player.getxPos() + 1, player.getyPos());
                if (move.getDirection() == Direction.SOUTH)
                    MoveToTile(player, player.getxPos(), player.getyPos() + 1);
                if (move.getDirection() == Direction.WEST)
                    MoveToTile(player, player.getxPos() - 1, player.getyPos());
                break;
            case 2: // ATTACK
                if (move.getDirection() == Direction.NORTH)
                    AttackTile(player, player.getxPos(), player.getyPos() - 1);
                if (move.getDirection() == Direction.EAST)
                    AttackTile(player, player.getxPos() + 1, player.getyPos());
                if (move.getDirection() == Direction.SOUTH)
                    AttackTile(player, player.getxPos(), player.getyPos() + 1);
                if (move.getDirection() == Direction.WEST)
                    AttackTile(player, player.getxPos() - 1, player.getyPos());
                break;
            case 3: // SKILL
                break;
        }
        if(playerInTurn.remove(Integer.valueOf(playerID)))
            playerInTurn.add(playerID);
        PrepareForNextRound();
    }

    public void MoveToTile(Player player, int xTarget, int yTarget){

    }

    public void AttackTile(Player player, int xTarget, int yTarget){

    }

    private void PrepareForNextRound()
    {
        for (int i = 0; i < players.size(); i++)
        {
            Player p = players.get(i);
            if (p.active)
            {
                p.PrepareForNextRound();
            }
        }
        for (Fire f : fires)
        {
            if(f != null)
                f.PrepareForNextRound();
        }
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static void setPlayers(List<Player> players) {
        GameState.players = players;
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
}
