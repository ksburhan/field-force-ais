package Game;

public class Fire extends MapObject {
    private int duration = 2;

    public Fire(char id, int x, int y){
        super(id, x, y);
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
