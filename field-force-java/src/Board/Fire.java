package Board;

import AI.AI;
import Game.GameConstants;
import Game.GameState;

public class Fire extends MapObject {
    private int duration = GameConstants.FIRE_DURATION_ON_MAP;

    public Fire(char id, int x, int y){
        super(id, x, y);
    }

    public Fire(char id, int x, int y, int duration){
        super(id, x, y);
        this.duration = duration;
    }

    public Fire(Fire other){
        super(other);
        this.duration = other.duration;
    }

    public void prepareForNextRound(GameState gameState)
    {
        duration--;
        if (duration <= 0)
        {
            destroy(gameState);
        }
    }

    public int reduceDuration(){
        return duration--;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
