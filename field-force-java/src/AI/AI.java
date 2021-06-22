package AI;
import Game.GameState;
import Game.Move;
import Game.MoveType;
import Game.Player;

import java.util.ArrayList;
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

    private int currentDepth;
    private Move bestMove;
    private Move globalBestMove;

    public Move getBestMove() throws InterruptedException, TimeoutException {
        try {
            for(int i = 0; i<30; i++){
                if(i > 0){
                    globalBestMove = bestMove;
                }
                currentDepth = i;
                checkTimelimit();
                int rating = maximizer(currentDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, currentState.getOwnPlayer(), currentState);
                System.out.println("rating" + rating);
            }
            System.out.println("reached depth" + currentDepth);
            return globalBestMove;
        }
        catch (TimeoutException te){
            System.out.println("Time is running out");
            return globalBestMove;
        }
    }

    private int maximizer(int depth, int alpha, int beta, Player player, GameState gameState){
        try {
            checkTimelimit();
        } catch (TimeoutException e) {
            return alpha;
        }
        if(depth == 0)
            return Evaluator.evaluate(gameState);
        List<Move> moves = gameState.getAllMoves(player.getPlayerNumber());

        for (Move m : moves){
            GameState copy = new GameState(gameState);
            copy.simulateNextGamestate(player.getPlayerNumber(), m);
            int rating = minimizer(depth - 1, alpha, beta, copy.getNextPlayer(), copy);
            if(rating > alpha){
                alpha = rating;
                if(alpha == 150)
                    return alpha;
                if(depth == currentDepth){
                    bestMove = m;
                }
            }
            if(alpha >= beta)
                return alpha;
        }
        return alpha;
    }

    private int minimizer(int depth, int alpha, int beta, Player player, GameState gameState){
        if(depth == 0)
            return Evaluator.evaluate(gameState);
        List<Move> moves = gameState.getAllMoves(player.getPlayerNumber());

        for (Move m : moves){
            GameState copy = new GameState(gameState);
            copy.simulateNextGamestate(player.getPlayerNumber(), m);
            int rating;
            if(copy.getNextPlayer().getPlayerNumber() == AI.ownPlayerID)
                rating = maximizer(depth - 1, alpha, beta, copy.getNextPlayer(), copy);
            else
                rating = minimizer(depth - 1, alpha, beta, copy.getNextPlayer(), copy);
            if(rating <= beta){
                beta = rating;
                if(beta == 150)
                    return beta;
            }
            if(alpha >= beta)
                return beta;
        }
        return beta;
    }

    private void checkTimelimit() throws TimeoutException {
        if (System.currentTimeMillis() - time_start > timelimit - 500) {
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
