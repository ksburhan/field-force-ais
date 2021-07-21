package AI;
import Game.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AI {

    public static AI instance;
    public static String playername = "java";
    public static Player ownPlayer;
    public static int skill1 = 0;
    public static int skill2 = 3;

    public static long time_start = 0;

    private GameState currentState;

    private int currentMaxDepth;
    private Move bestMove;
    private Move globalBestMove;

    public Move getBestMove() throws InterruptedException, TimeoutException {
        try {
            for(int d = 0; d<30; d++){
                if(d > 0){
                    globalBestMove = bestMove;
                }
                currentMaxDepth = 1 + d;
                checkTimelimit();
                int rating = minmax(currentMaxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, currentState.getOwnPlayer(), currentState);
                System.out.println("rating" + rating);
            }
            System.out.println("reached depth" + currentMaxDepth);
            return globalBestMove;
        }
        catch (TimeoutException te){
            System.out.println("Time is running out");
            return globalBestMove;
        }
    }

    private int minmax(int depth, int alpha, int beta, Player currentplayer, GameState gameState) throws TimeoutException {
        checkTimelimit();
        boolean maximizer;
        maximizer = currentplayer.getPlayerNumber() == GameConstants.OWN_PLAYER_ID;
        if(depth == 0)
            return Evaluator.evaluate(gameState);

        int rating;
        List<Move> moves = gameState.getAllMoves(currentplayer.getPlayerNumber());

        for (Move m : moves){
            GameState copy = new GameState(gameState);
            copy.simulateNextGamestate(currentplayer.getPlayerNumber(), m);
            rating = minmax(depth - 1, alpha, beta, copy.getNextPlayer(), copy);
            if(maximizer){
                if (rating > alpha){
                    alpha = rating;
                    if(depth == currentMaxDepth){
                        bestMove = m;
                    }
                }
                if(alpha >= beta) {
                    return alpha;
                }
            } else {
                if(rating <= beta){
                    beta = rating;
                    if(alpha >= beta){
                        return beta;
                    }
                }
            }
        }
        if(maximizer)
            return alpha;
        else
            return beta;
    }
/*
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
            }
            if(alpha >= beta)
                return alpha;
=======
            if(!Constants.VERBOSE) System.out.println("Time is running out");
            return bestMove;
>>>>>>> 69bd98a06daab227fe50001fab833ca73a8b63f5
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
*/
    private void checkTimelimit() throws TimeoutException {
        if (System.currentTimeMillis() - AI.time_start > GameConstants.TIME_LIMIT - 500) {
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
