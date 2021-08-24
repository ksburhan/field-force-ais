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
        GameConstants.GAMEMODE = gamemode;
        GameConstants.TIME_LIMIT = timelimit * 1000;
        GameConstants.OWN_PLAYER_ID = ownID;
        packet.readConfig();
    }

    public static void handlePlayerinformation(Packet packet) throws Exception {
        List<Player> players = packet.readPlayers();
        Player.ALL_PLAYERS = players;
        AI.ownPlayer = Player.ALL_PLAYERS.get(GameConstants.OWN_PLAYER_ID-1);
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
        AI.instance.setCurrentState(new GameState(new GameField(dimension, map, Player.ALL_PLAYERS), Player.ALL_PLAYERS, playerInTurn, fires, walls, consumables));
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
        AI.instance.setCurrentState(new GameState(new GameField(dimension, map, currentPlayers), currentPlayers, playerInTurn, fires, walls, consumables));
    }

    public static void handleMovedistribution(Packet packet) throws Exception {
        int lastPlayerID = packet.readInt();
        Move move = packet.readMove();
        String log = packet.readString();
        if(!GameConstants.VERBOSE) System.out.println(log);
        AI.instance.getCurrentState().setLastMove(move);
    }

    public static void handleErrors(Packet packet) throws Exception {
        String errorMessage = packet.readString();
        if(!GameConstants.VERBOSE) System.out.println("Errormessage: " + errorMessage);
    }

    public static void handleGameover(Packet packet) throws Exception {
        String message = packet.readString();
        int winner_id = packet.readInt();
        boolean won = false;
        if (winner_id == AI.ownPlayer.getPlayerNumber())
            won = true;
        if(!GameConstants.VERBOSE) System.out.println(message + " Won?: " + won);
    }
}
