package AI;

import Game.GameConstants;
import Game.GameState;
import Game.Player;

public class Evaluator {


    public static float evaluate(GameState gameState){
        if(gameState.isGameOver()) {
            System.out.println("gameover");
            if (gameState.getCurrentPlayers().get(0).id == AI.ownPlayer.id)
                return 1000;
            else
                return -1000;
        }
        Player own = null;
        for (Player p : gameState.getCurrentPlayers())
            if (p.getPlayerNumber() == GameConstants.OWN_PLAYER_ID)
                own = p;
        int enemyHP = 0;
        int allyHP = 0;
        int enemydistance = 100;
        int skill1cd = own.getSkill1().getCooldownLeft();
        int skill2cd = own.getSkill2().getCooldownLeft();
        for (Player p : gameState.getCurrentPlayers()) {
            int hp = p.getHp();
            int shield = p.getShield();
            if (p.getPlayerNumber() == GameConstants.OWN_PLAYER_ID){
                allyHP += hp;
                allyHP += shield;
            }else{
                enemyHP += hp;
                enemyHP += shield;
                int x = Math.abs(p.getxPos() - own.getxPos());
                int y = Math.abs(p.getyPos() - own.getyPos());
                int d = x + y;
                if(d < enemydistance)
                    enemydistance = d + 1;
            }
        }
        //float rating = ((allyHP - enemyHP*3) + (1.0f/enemydistance * 2) + skill1cd + skill2cd);
        //System.out.println(rating);
        return 1.0f/enemydistance;
        //return rating;
    }

}
