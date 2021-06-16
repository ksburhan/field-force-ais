package Board;

import AI.AI;

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

    public MapObject(MapObject other){
        this.id = other.id;
        this.xPos = other.xPos;
        this.yPos = other.yPos;
    }

    public void destroy(){
        int x = getxPos();
        int y = getyPos();
        AI.instance.getCurrentState().getCurrentField().getFieldChars()[x][y] = '0';
        AI.instance.getCurrentState().getCurrentField().getField()[x][y].setContent(new MapObject('0', x, y));
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
