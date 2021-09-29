package AI;
import Game.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AI {

    public static AI instance;
    public static String playername = "alpha";
    public static Player ownPlayer;
    public static int skill1 = 0;
    public static int skill2 = 4;

    public static long time_start = 0;

    private GameState currentState;

    private int maxDepth = 5;
    private Move globalBestMove;

    public Move getBestMove() throws InterruptedException, TimeoutException {
        try {
            Move move = minmaxFirst(1, Float.MIN_VALUE, Float.MAX_VALUE, currentState.getOwnPlayer(), currentState);
            for(int d = 2; d <= maxDepth; d++) {
                System.out.println("Looking from start to depth: " + d);
                move = minmaxFirst(d, Integer.MIN_VALUE, Integer.MAX_VALUE, currentState.getOwnPlayer(), currentState);
            }
            return move;
        }
        catch (TimeoutException te){
            System.out.println("Time is running out");
            return globalBestMove;
        }
    }

    private Move minmaxFirst(int depth, float alpha, float beta, Player currentplayer, GameState gameState) throws TimeoutException {
        boolean maximizer;
        maximizer = currentplayer.getPlayerNumber() == GameConstants.OWN_PLAYER_ID;
        float bestRating = Float.MIN_VALUE;
        if(!maximizer)
            bestRating = Integer.MAX_VALUE;

        List<Move> moves = gameState.getAllMoves(currentplayer.getPlayerNumber());
        Move gmove = moves.get(0);

        for(Move m : moves){
            GameState copy = new GameState(gameState);
            copy.simulateNextGamestate(currentplayer.getPlayerNumber(), m);
            float rating = minmax(depth - 1, alpha, beta, copy.getNextPlayer(), copy);
            if(maximizer && rating >= bestRating){
                bestRating = rating;
                gmove = m;
                globalBestMove = m;
            } else if (!maximizer && rating <= bestRating){
                bestRating = rating;
                gmove = m;
                globalBestMove = m;
            }
        }
        return gmove;
    }

    private float minmax(int depth, float alpha, float beta, Player currentplayer, GameState gameState) throws TimeoutException {
        checkTimelimit();
        boolean maximizer;
        maximizer = currentplayer.getPlayerNumber() == GameConstants.OWN_PLAYER_ID;
        if (depth == 0 || gameState.isGameOver())
            return Evaluator.evaluate(gameState);

        if (maximizer) {
            float maxScore = Float.MIN_VALUE;
            List<Move> moves = gameState.getAllMoves(currentplayer.getPlayerNumber());
            for (Move m : moves) {
                GameState copy = new GameState(gameState);
                copy.simulateNextGamestate(currentplayer.getPlayerNumber(), m);

                float rating = minmax(depth - 1, alpha, beta, copy.getNextPlayer(), copy);
                maxScore = Math.max(maxScore, rating);
                alpha = Math.max(alpha, rating);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxScore;
        } else {
            float minScore = Integer.MAX_VALUE;
            List<Move> moves = gameState.getAllMoves(currentplayer.getPlayerNumber());
            for (Move m : moves) {
                GameState copy = new GameState(gameState);
                copy.simulateNextGamestate(currentplayer.getPlayerNumber(), m);

                float rating = minmax(depth - 1, alpha, beta, copy.getNextPlayer(), copy);
                minScore = Math.min(minScore, rating);
                beta = Math.min(beta, rating);
                if (beta < alpha) {
                    break;
                }
            }
            return minScore;
        }
    }

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
