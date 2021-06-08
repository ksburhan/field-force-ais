package Connection;

import AI.AI;
import Board.Fire;
import Board.GameField;
import Board.Wall;
import Game.GameState;
import Game.Move;
import Game.Player;

import java.util.ArrayList;
import java.util.List;

public class ClientHandle {

    public static void handleGamemode(Packet packet) throws Exception {
        int gamemode = packet.readInt();
        int ownID = packet.readInt();
        System.out.println("CLIENTHANDLE gamemode: " + gamemode);
    }

    public static void handlePlayerinformation(Packet packet) throws Exception {
        List<Player> players = packet.readPlayers();
        GameState.setPlayers(players);
        System.out.println("CLIENTHANDLE " + players.size());
    }

    public static void handleInitialMap(Packet packet) throws Exception {
        int dimension = packet.readInt();
        char[][] map = packet.readMap(dimension);
        GameField gameField = new GameField(dimension, map);
        List<Integer> playerInTurn = packet.readPlayerInTurn();
        AI.instance.setCurrentState(new GameState(gameField, playerInTurn));
    }

    public static void handleMoveRequest(Packet packet) throws Exception {
        int ownId = packet.readInt();
        String message = packet.readString();
        System.out.println("CLIENTHANDLE " + ownId + " " + message);
        Move bestMove = AI.instance.getBestMove(AI.instance.getCurrentState().getAllMoves(ownId));
        ClientSend.sendMovereply(ownId, bestMove);
    }

    public static void handleMovedistribution(Packet packet) throws Exception {
        int lastPlayerID = packet.readInt();
        Move move = packet.readMove();
    }

    public static void handleNewGamestate(Packet packet) throws Exception {
        int dimension = packet.readInt();
        char[][] map = packet.readMap(dimension);
        List<Player> players = packet.readPlayers();
        List<Integer> playerInTurn = packet.readPlayerInTurn();
        List<Fire> fires = packet.readFires();
        List<Wall> walls = packet.readWalls();
        AI.instance.setCurrentState(new GameState(new GameField(dimension, map), players, playerInTurn, fires, walls));
    }

    public static void handleErrors(Packet packet) throws Exception {
        String errorMessage = packet.readString();
        System.out.println("CLIENTHANDLE errormessage: " + errorMessage);
    }

    public static void handleGameover(Packet packet) throws Exception {
        String message = packet.readString();
        System.out.println("CLIENTHANDLE " + message);
    }
}
