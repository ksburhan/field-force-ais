package Board;

import AI.AI;
import Game.GameConstants;

public class Fire extends MapObject {
    private int duration = GameConstants.FIRE_DURATION_ON_MAP;

    public Fire(char id, int x, int y){
        super(id, x, y);
    }

    public Fire(char id, int x, int y, int duration){
        super(id, x, y);
        this.duration = duration;
    }

    public void PrepareForNextRound()
    {
        duration--;
        if (duration <= 0)
        {
            Destroy();
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
