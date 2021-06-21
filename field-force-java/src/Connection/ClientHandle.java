package Connection;

import AI.AI;
import Board.Consumable;
import Board.Fire;
import Board.GameField;
import Board.Wall;
import Game.*;

import java.util.List;

public class ClientHandle {

    public static void handleGamemode(Packet packet) throws Exception {
        int gamemode = packet.readInt();
        int timelimit = packet.readInt();
        int ownID = packet.readInt();
        if(gamemode == 1){
            AI.skill1 = packet.readInt();
            AI.skill2 = packet.readInt();
        }
        AI.gamemode = gamemode;
        AI.timelimit = timelimit * 1000;
        AI.ownPlayerID = ownID;
        packet.readConfig();
    }

    public static void handlePlayerinformation(Packet packet) throws Exception {
        List<Player> players = packet.readPlayers();
        GameState.setPlayers(players);
        AI.ownPlayer = GameState.getPlayers().get(AI.ownPlayerID-1);
        AI.ownPlayer.setSkill1(Skill.getSkill(AI.skill1));
        AI.ownPlayer.setSkill2(Skill.getSkill(AI.skill2));
    }

    public static void handleInitialMap(Packet packet) throws Exception {
        int dimension = packet.readInt();
        char[][] map = packet.readMap(dimension);
        List<Integer> playerInTurn = packet.readPlayerInTurn();
        List<Fire> fires = packet.readFires();
        List<Wall> walls = packet.readWalls();
        List<Consumable> consumables = packet.readConsumables();
        AI.instance.setCurrentState(new GameState(new GameField(dimension, map), GameState.getPlayers(), playerInTurn, fires, walls, consumables));
    }

    public static void handleMoveRequest(Packet packet) throws Exception {
        int ownId = packet.readInt();
        AI.time_start = System.currentTimeMillis();
        Move bestMove = AI.instance.getBestMove();
        ClientSend.sendMovereply(ownId, bestMove);
    }

    public static void handleNewGamestate(Packet packet) throws Exception {
        int dimension = packet.readInt();
        char[][] map = packet.readMap(dimension);
        List<Player> currentPlayers = packet.readPlayers();
        List<Integer> playerInTurn = packet.readPlayerInTurn();
        List<Fire> fires = packet.readFires();
        List<Wall> walls = packet.readWalls();
        List<Consumable> consumables = packet.readConsumables();
        AI.instance.setCurrentState(new GameState(new GameField(dimension, map), currentPlayers, playerInTurn, fires, walls, consumables));
    }

    public static void handleMovedistribution(Packet packet) throws Exception {
        int lastPlayerID = packet.readInt();
        Move move = packet.readMove();
        String log = packet.readString();
        System.out.println(log);
        AI.instance.getCurrentState().setLastMove(move);
    }

    public static void handleErrors(Packet packet) throws Exception {
        String errorMessage = packet.readString();
        System.out.println("CLIENTHANDLE errormessage: " + errorMessage);
    }

    public static void handleGameover(Packet packet) throws Exception {
        String message = packet.readString();
        System.out.println("CLIENTHANDLE Gameover: " + message);
    }
}
