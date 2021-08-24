package Board;
import Game.GameConstants;
import Game.Player;

import java.util.List;

public class GameField {

    private int dimension;
    private Tile[][] field;
    private char[][] fieldChars;

    public GameField(int dimension, char[][] map, List<Player> currentPlayers){
        this.dimension = dimension;
        this.field = createField(map, currentPlayers);
        this.fieldChars = map;
    }

    public GameField(GameField other){
        this.dimension = other.dimension;
        this.field = new Tile[this.dimension][this.dimension];
        for(int y=0; y<this.dimension; y++)
            for(int x=0; x<this.dimension; x++)
                this.field[x][y] = new Tile(other.field[x][y]);
        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                if(y > 0)
                    this.field[x][y].setnTile(this.field[x][y-1]);
                if(x != dimension-1)
                    this.field[x][y].seteTile(this.field[x+1][y]);
                if(y != dimension-1)
                    this.field[x][y].setsTile(this.field[x][y+1]);
                if(x > 0)
                    this.field[x][y].setwTile(this.field[x-1][y]);
            }
        }
        this.fieldChars = new char[this.dimension][this.dimension];
        for(int y=0; y<this.dimension; y++)
            for(int x=0; x<this.dimension; x++)
                this.fieldChars[x][y] = other.fieldChars[x][y];
    }

    private Tile[][] createField(char[][] map, List<Player> currentPlayers) {
        Tile[][] field = new Tile[dimension][dimension];
        for (int y = 0; y < dimension; y++)
        {
            for (int x = 0; x < dimension; x++)
            {
                MapObject mapObject = null;
                switch (map[x][y]) {
                    case '1' -> {
                        mapObject = getPlayer(currentPlayers, 1);
                        mapObject.setPos(x, y);
                    }
                    case '2' -> {
                        mapObject = getPlayer(currentPlayers, 2);
                        mapObject.setPos(x, y);
                    }
                    case '3' -> {
                        mapObject = getPlayer(currentPlayers, 3);
                        mapObject.setPos(x, y);
                    }
                    case '4' -> {
                        mapObject = getPlayer(currentPlayers, 4);
                        mapObject.setPos(x, y);
                    }
                    case 'f' -> mapObject = new Fire(map[x][y], x, y);
                    case '-' -> mapObject = new Wall(map[x][y], x, y);
                    case '0' -> mapObject = new MapObject(map[x][y], x, y);
                    default -> {
                        for (char c : GameConstants.VALID_CONSUMEABLES) {
                            if (map[x][y] == c) {
                                mapObject = new Consumable(map[x][y], x, y);
                                break;
                            }
                        }
                    }
                }
                field[x][y] = new Tile(x,y, mapObject);
            }
        }

        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                if(y > 0)
                    field[x][y].setnTile(field[x][y-1]);
                if(x != dimension-1)
                    field[x][y].seteTile(field[x+1][y]);
                if(y != dimension-1)
                    field[x][y].setsTile(field[x][y+1]);
                if(x > 0)
                    field[x][y].setwTile(field[x-1][y]);
            }
        }

        return field;
    }

    public void printMap(){
        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                if(!GameConstants.VERBOSE) System.out.print(this.field[x][y].getContent().id + " ");
            }
            if(!GameConstants.VERBOSE) System.out.println();
        }
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public Tile[][] getField() {
        return field;
    }

    public void setField(Tile[][] field) {
        this.field = field;
    }

    public char[][] getFieldChars() {
        return fieldChars;
    }

    public void setFieldChars(char[][] fieldChars) {
        this.fieldChars = fieldChars;
    }

    private Player getPlayer(List<Player> currentPlayers, int number){
        for (Player p : currentPlayers) {
            if (number == p.getPlayerNumber())
                return p;
        }
        return null;
    }
}
