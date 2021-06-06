package Board;

public class MapObject{
    public char id;
    private int xPos;
    private int yPos;

    public MapObject(char id, int x, int y){
        this.id = id;
        this.xPos = x;
        this.yPos = y;
    }

    public MapObject(char id) {
        this.id = id;
    }

    public char getId() {
        return id;
    }

    public void setId(char id) {
        this.id = id;
    }

    public void setPos(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
}
