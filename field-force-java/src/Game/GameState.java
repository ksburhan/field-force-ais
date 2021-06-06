package Game;

import Board.Fire;
import Board.GameField;
import Board.Tile;
import Board.Wall;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private static List<Player> players = new ArrayList<>();
    private int playerInTurn;


    private List<Fire> fires = new ArrayList<>();
    private List<Wall> walls = new ArrayList<>();

    private GameField currentField;

    public GameState(GameField gameField, int playerInTurn){
        this.currentField = gameField;
        this.playerInTurn = playerInTurn;
    }

    public GameState(GameField gameField, List<Player> players, int playerInTurn, List<Fire> fires, List<Wall> walls){
        this.currentField = gameField;
        GameState.players = players;
        this.playerInTurn = playerInTurn;
        this.fires = fires;
        this.walls = walls;
    }

    public int nextPlayer(){
        return (playerInTurn % players.size()) + 1;
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
        for (char x : GameConstants.validTargets){
            if(x == c){
                return true;
            }
        }
        return false;
    }

    public void simulateNextGamestate(Move move){

    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static void setPlayers(List<Player> players) {
        GameState.players = players;
    }

    public int getPlayerInTurn() {
        return playerInTurn;
    }

    public void setPlayerInTurn(int playerInTurn) {
        this.playerInTurn = playerInTurn;
    }

    public GameField getCurrentField() {
        return currentField;
    }

    public void setCurrentField(GameField currentField) {
        this.currentField = currentField;
    }
}
