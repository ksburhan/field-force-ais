package AI;
import Game.GameState;
import Game.Move;
import Game.MoveType;
import Game.Player;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AI {

    public static AI instance;
    public static String playername = "playername";
    public static int ownPlayerID;
    public static Player ownPlayer;
    public static int skill1 = 0;
    public static int skill2 = 3;

    public static int gamemode = 0;
    public static int timelimit = 0;
    public static long time_start = 0;

    private GameState currentState;

    public Move getBestMove() throws InterruptedException, TimeoutException {
        Move bestMove = null;
        try {
            checkTimelimit();
            List<Move> moves = AI.instance.getCurrentState().getAllMoves(ownPlayerID);
            Random rand = new Random();
            bestMove = moves.get(rand.nextInt(moves.size()));
            return bestMove;
        }
        catch (TimeoutException te){
            if(!Constants.VERBOSE) System.out.println("Time is running out");
            return bestMove;
        }
    }

    private void checkTimelimit() throws TimeoutException {
        if (System.currentTimeMillis() - AI.time_start > AI.timelimit - 500) {
            throw new TimeoutException();
        }
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
