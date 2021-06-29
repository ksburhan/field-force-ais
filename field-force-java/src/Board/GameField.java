package Board;
import AI.Constants;
import Game.GameState;

public class GameField {

    private int dimension;
    private Tile[][] field;
    private char[][] fieldChars;

    public GameField(int dimension, char[][] map){
        this.dimension = dimension;
        this.field = createField(map);
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

    private Tile[][] createField(char[][] map) {
        Tile[][] field = new Tile[dimension][dimension];
        for (int y = 0; y < dimension; y++)
        {
            for (int x = 0; x < dimension; x++)
            {
                MapObject mapObject;
                switch (map[x][y]) {
                    case '1' -> {
                        mapObject = GameState.getPlayers().get(0);
                        mapObject.setPos(x, y);
                    }
                    case '2' -> {
                        mapObject = GameState.getPlayers().get(1);
                        mapObject.setPos(x, y);
                    }
                    case '3' -> {
                        mapObject = GameState.getPlayers().get(2);
                        mapObject.setPos(x, y);
                    }
                    case '4' -> {
                        mapObject = GameState.getPlayers().get(3);
                        mapObject.setPos(x, y);
                    }
                    case 'f' -> mapObject = new Fire(map[x][y], x, y);
                    case '-' -> mapObject = new Wall(map[x][y], x, y);
                    default -> mapObject = new MapObject(map[x][y], x, y);
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
                if(!Constants.VERBOSE) System.out.print(this.field[x][y].getContent().id + " ");
            }
            if(!Constants.VERBOSE) System.out.println();
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
}
