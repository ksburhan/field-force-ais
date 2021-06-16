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
    public static int skill1 = 4;
    public static int skill2 = 5;

    public static int gamemode = 0;
    public static int timelimit = 0;
    public static long time_start = 0;

    private GameState currentState;

    public Move getBestMove(List<Move> moves) throws InterruptedException, TimeoutException {
        TimeUnit.SECONDS.sleep(1);
        Random rand = new Random();
        for (Move move : moves) {
            checkTimelimit();
            if (move.getType() == MoveType.SKILL) {
                new GameState(currentState).simulateNextGamestate(ownPlayerID, move);
                return move;
            }
        }
        return moves.get(rand.nextInt(moves.size()));
    }

    private void checkTimelimit() throws TimeoutException {
        if (System.currentTimeMillis() - AI.time_start > AI.timelimit) {
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
