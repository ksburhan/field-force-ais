package AI;
import Game.GameState;
import Game.Move;

import java.util.List;
import java.util.Random;

public class AI {

    public static AI instance;
    public static String playername = "playername";

    private GameState currentState;

    public Move getBestMove(List<Move> moves){
        Random rand = new Random();
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
