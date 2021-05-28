package Game;

public class Fire extends MapObject {
    public int duration;

    public Fire(){
        duration = 2;
    }

    public int reduceDuration(){
        return duration--;
    }
}
