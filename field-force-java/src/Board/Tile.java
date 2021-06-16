package Board;

import Game.Player;

public class Tile {

    private int xPos;
    private int yPos;
    private MapObject content;

    private Tile nTile = null;
    private Tile eTile = null;
    private Tile sTile = null;
    private Tile wTile = null;

    public Tile(int x, int y, MapObject content){
        this.xPos = x;
        this.yPos = y;
        this.content = content;
    }

    public Tile(Tile other){
        this.xPos = other.xPos;
        this.yPos = other.yPos;
        if(other.content instanceof Player)
            this.content = new Player((Player) other.content);
        else if(other.content instanceof Wall)
            this.content = new Wall((Wall) other.content);
        else if(other.content instanceof Fire)
            this.content = new Fire((Fire) other.content);
        else if(other.content instanceof Consumable)
            this.content = new Consumable((Consumable) other.content);
        else
            this.content = new MapObject(other.content);
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

    public void setPos(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public MapObject getContent() {
        return content;
    }

    public void setContent(MapObject content) {
        this.content = content;
    }

    public Tile getnTile() {
        return nTile;
    }

    public void setnTile(Tile nTile) {
        this.nTile = nTile;
    }

    public Tile geteTile() {
        return eTile;
    }

    public void seteTile(Tile eTile) {
        this.eTile = eTile;
    }

    public Tile getsTile() {
        return sTile;
    }

    public void setsTile(Tile sTile) {
        this.sTile = sTile;
    }

    public Tile getwTile() {
        return wTile;
    }

    public void setwTile(Tile wTile) {
        this.wTile = wTile;
    }
}
