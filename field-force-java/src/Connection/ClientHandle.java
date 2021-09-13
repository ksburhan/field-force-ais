package Connection;

import AI.AI;
import Board.Consumable;
import Board.Fire;
import Board.GameField;
import Board.Wall;
import Game.*;

import java.util.List;

public class ClientHandle {

    /**
     * @param packet
     * @throws Exception
     * Reads Pakettype 2. Gamemode, Timelimit and own player number
     * if gamemode 1, two random skills are assigned
     */
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

    /**
     * @param packet
     * @throws Exception
     * Reads Pakettype 3. information of all other players
     */
    public static void handlePlayerinformation(Packet packet) throws Exception {
        List<Player> players = packet.readPlayers();
        Player.ALL_PLAYERS = players;
        AI.ownPlayer = Player.ALL_PLAYERS.get(GameConstants.OWN_PLAYER_ID-1);
        AI.ownPlayer.setSkill1(Skill.getSkill(AI.skill1));
        AI.ownPlayer.setSkill2(Skill.getSkill(AI.skill2));
    }

    /**
     * @param packet
     * @throws Exception
     * reads Pakettype 4. reads all information to create new gamestate object as the current state
     */
    public static void handleInitialMap(Packet packet) throws Exception {
        int dimension = packet.readInt();
        char[][] map = packet.readMap(dimension);
        List<Integer> playerInTurn = packet.readPlayerInTurn();
        List<Fire> fires = packet.readFires();
        List<Wall> walls = packet.readWalls();
        List<Consumable> consumables = packet.readConsumables();
        AI.instance.setCurrentState(new GameState(new GameField(dimension, map, Player.ALL_PLAYERS), Player.ALL_PLAYERS, playerInTurn, fires, walls, consumables));
    }

    /**
     * @param packet
     * @throws Exception
     * reads Pakettype 5. Only used to start looking for a move to reply with
     */
    public static void handleMoveRequest(Packet packet) throws Exception {
        int ownId = packet.readInt();
        AI.time_start = System.currentTimeMillis();
        Move bestMove = AI.instance.getBestMove();
        ClientSend.sendMovereply(ownId, bestMove);
    }

    /**
     * @param packet
     * @throws Exception
     * reads Pakettype 7. reads information needed to create new gamestate object after last move has been calculated
     */
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

    /**
     * @param packet
     * @throws Exception
     * reads Pakettype 8. tells the last move that has been calculated
     */
    public static void handleMovedistribution(Packet packet) throws Exception {
        int lastPlayerID = packet.readInt();
        Move move = packet.readMove();
        String log = packet.readString();
        if(!GameConstants.VERBOSE) System.out.println(log);
        AI.instance.getCurrentState().setLastMove(move);
    }

    /**
     * @param packet
     * @throws Exception
     * reads Pakettype 9. received when own player did wrong move, gets disqualified but connected
     */
    public static void handleErrors(Packet packet) throws Exception {
        String errorMessage = packet.readString();
        if(!GameConstants.VERBOSE) System.out.println("Errormessage: " + errorMessage);
    }

    /**
     * @param packet
     * @throws Exception
     * reads Pakettype 10. received when game is over. tells winner number
     */
    public static void handleGameover(Packet packet) throws Exception {
        String message = packet.readString();
        int winner_id = packet.readInt();
        boolean won = false;
        if (winner_id == AI.ownPlayer.getPlayerNumber())
            won = true;
        if(!GameConstants.VERBOSE) System.out.println(message + " Won?: " + won);
    }

    // when new server packets are created, just add new function to handle new packet
}
