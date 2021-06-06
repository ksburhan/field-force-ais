package Board;

public class Fire extends MapObject {
    private int duration = 2;

    public Fire(char id, int x, int y){
        super(id, x, y);
    }

    public Fire(char id, int x, int y, int duration){
        super(id, x, y);
        this.duration = duration;
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
