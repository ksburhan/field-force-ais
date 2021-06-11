package AI;
import Game.GameState;
import Game.Move;
import Game.MoveType;
import Game.Player;

import java.util.List;
import java.util.Random;

public class AI {

    public static AI instance;
    public static String playername = "playername";
    public static int ownPlayerID;
    public static Player ownPlayer;
    public static int skill1 = 0;
    public static int skill2 = 1;

    private GameState currentState;

    public Move getBestMove(List<Move> moves){
        Random rand = new Random();
        for (Move move : moves) {
            if (move.getType() == MoveType.SKILL)
                return move;
        }
        return moves.get(rand.nextInt(moves.size()));
    }

    public static AI getInstance(){
        if(instance == null) {
            instance = new AI();
        }
        return instance;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }
}
