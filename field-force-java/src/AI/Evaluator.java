package AI;

import Game.GameState;
import Game.Player;

public class Evaluator {


    public static int evaluate(GameState gameState){
        int enemyHP = 0;
        int allyHP = 0;
        for (Player p : gameState.getCurrentPlayers()) {
            int hp = p.getHp();
            int shield = p.getShield();
            if (p.getPlayerNumber() == AI.ownPlayerID){
                allyHP += hp;
                allyHP += shield;
            }else{
                enemyHP += hp;
                enemyHP += shield;
            }
        }
        return allyHP - enemyHP*2;
    }

}
