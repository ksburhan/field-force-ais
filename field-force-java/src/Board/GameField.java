package Board;
import Game.GameState;

public class GameField {

    private int dimension;
    private Tile[][] field;

    public GameField(int dimension, char[][] map){
        this.dimension = dimension;
        this.field = createField(map);
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
}
