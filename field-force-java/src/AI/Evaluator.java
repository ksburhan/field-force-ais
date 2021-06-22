package AI;

import Game.GameState;
import Game.Player;

public class Evaluator {


    public static int evaluate(GameState gameState){
        int enemyHP = 0;
        int allyHP = 0;
        for (Player p : gameState.getCurrentPlayers()) {
            int hp = getPlayerValue(p);
            if (p.getPlayerNumber() == AI.ownPlayerID){
                allyHP =+ hp;
            }else{
                enemyHP =+ hp;
            }
        }
        return allyHP - enemyHP;
    }

    private static int getPlayerValue(Player player){
        return player.getHp();
    }

}
