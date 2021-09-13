package Connection;

import AI.AI;
import Board.Consumable;
import Board.Fire;
import Board.Wall;
import Game.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class Packet {

    private List<Byte> buffer;
    private byte[] readableBuffer;
    private int readPos;

    public Packet() {
        buffer = new ArrayList<>();
        readPos = 0;
    }

    public Packet(byte[] data) {
        buffer = new ArrayList<>();
        readPos = 0;

        setBytes(data);
    }

    private void setBytes(byte[] data) {
        write(data);
        readableBuffer = toByteArray();
    }

    /**
     * @return
     * @throws Exception
     * reads 4 bytes and increased read pos
     */
    public int readInt() throws Exception {
        if (buffer.size() > readPos)
        {
            int value = convertToInt(splitMessage(readableBuffer, readPos, readPos+3));
            readPos += 4;
            return value;
        }
        else
        {
            throw new Exception("Could not read value of type 'int'!");
        }
    }

    /**
     * @return
     * @throws Exception
     * reads 4 bytes for the length of the string
     * reads the amount of bytes that are read in the length
     */
    public String readString() throws Exception {
        try
        {
            int length = readInt();
            String message = new String(toByteArray(), readPos, length);
            if (message.length() > 0)
            {
                readPos += length;
            }
            return message;
        }
        catch (Exception e)
        {
            throw new Exception("Could not read value of type 'string'!");
        }
    }

    /**
     * @param dimension
     * @return
     * @throws Exception
     * reads all the chars for the map and puts in appropriate 2d array
     */
    public char[][] readMap(int dimension) throws Exception {
        char[][] map = new char[dimension][dimension];
        for (int y = 0; y < dimension; y++)
        {
            for (int x = 0; x < dimension; x++)
            {
                map[x][y] = (char) readInt();
            }
        }
        return map;
    }

    /**
     * @return
     * @throws Exception
     * reads playerifnromation and returns a list of player objects
     */
    public List<Player> readPlayers() throws Exception {
        List<Player> players = new ArrayList<>();
        int playerCount = readInt();
        for (int i = 0; i < playerCount; i++){
            int playerNumber = readInt();
            String playerName = readString();
            int hp = readInt();
            int shield = readInt();
            int xPos = readInt();
            int yPos = readInt();
            Skill skill1 = readSkill();
            Skill skill2 = readSkill();
            players.add(new Player((char)(playerNumber+'0'), playerNumber, playerName, hp, shield, xPos, yPos, skill1, skill2));        }
        return players;
    }

    /**
     * @return
     * @throws Exception
     * reads the order in which players are supposed to play
     * index 0 is next player to move
     */
    public List<Integer> readPlayerInTurn() throws Exception {
        List<Integer> playerInTurn = new ArrayList<>();
        int playerCount = readInt();
        for (int i = 0; i < playerCount; i++){
            int playerNumber = readInt();
            playerInTurn.add(playerNumber);
        }
        return playerInTurn;
    }

    /**
     * @return
     * @throws Exception
     * reads all the fires that are available on the map
     */
    public List<Fire> readFires() throws Exception {
        List<Fire> fires = new ArrayList<>();
        int fireCount = readInt();
        for (int i = 0; i < fireCount; i++)
        {
             int x = readInt();
             int y = readInt();
             int duration = readInt();
             fires.add(new Fire('f',x,y, duration));
        }
        return fires;
    }

    /**
     * @return
     * @throws Exception
     * reads all the walls that are available on the map
     */
    public List<Wall> readWalls() throws Exception {
        List<Wall> walls = new ArrayList<>();
        int wallCount = readInt();
        for (int i = 0; i < wallCount; i++)
        {
            int x = readInt();
            int y = readInt();
            int hp = readInt();
            walls.add(new Wall('-', x, y, hp));
        }
        return walls;
    }

    /**
     * @throws Exception
     * reads the full config needed to play the game
     */
    public void readConfig() throws Exception {
        // read basics
        GameConstants.HP = readInt();
        GameConstants.SHIELD = readInt();

        GameConstants.ATTACK_DAMAGE = readInt();
        GameConstants.WALK_IN_PLAYER_DAMAGE = readInt();
        GameConstants.PLAYER_WALKED_INTO_DAMAGE = readInt();

        GameConstants.FIRE_DURATION_ON_MAP = readInt();
        GameConstants.ON_FIRE_EFFECT_DURATION = readInt();
        GameConstants.ON_FIRE_DAMAGE = readInt();

        GameConstants.WALL_HP = readInt();
        GameConstants.WALK_IN_WALL_DAMAGE = readInt();
        GameConstants.WALL_TAKE_DAMAGE = readInt();

        // read consumeables
        Consumable.allConsumables = readConfigConsumables();

        // read skills
        Skill.allSkills = readConfigSkills();
    }

    /**
     * @return
     * @throws Exception
     * reads all valid consumables and puts in global list GameConstants.VALID_CONSUMABLES
     */
    public List<Consumable> readConfigConsumables() throws Exception {
        List<Consumable> consumables = new ArrayList<>();
        int consumableCount = readInt();
        for (int i = 0; i < consumableCount; i++)
        {
            char id = (char) readInt();
            String conName = readString();
            int healing = readInt();
            int shield = readInt();
            consumables.add(new Consumable(id, conName, healing, shield));
            GameConstants.VALID_CONSUMEABLES.add(id);
        }
        return consumables;
    }

    /**
     * @return
     * @throws Exception
     * reads all the consumables that are available on the map
     */
    public List<Consumable> readConsumables() throws Exception {
        List<Consumable> consumables = new ArrayList<>();
        int consumableCount = readInt();
        for (int i = 0; i < consumableCount; i++)
        {
            char id = (char) readInt();
            int x = readInt();
            int y = readInt();
            consumables.add(new Consumable(id, x, y));
        }
        return consumables;
    }

    /**
     * @return
     * @throws Exception
     * reads all valid skills
     */
    public List<Skill> readConfigSkills() throws Exception {
        List<Skill> skills = new ArrayList<>();
        int skillCount = readInt();
        for (int i = 0; i < skillCount; i++)
        {
            int id = readInt();
            String name = readString();
            int cooldown = readInt();
            int range = readInt();
            int value = readInt();
            int type = readInt();
            skills.add(new Skill(id, name, cooldown, range, value, SkillType.fromInt(type)));
        }
        return skills;
    }

    /**
     * @return
     * @throws Exception
     * reads a move object
     */
    public Move readMove() throws Exception {
        MoveType type =  MoveType.fromInt(readInt());
        Direction direction = Direction.fromInt(readInt());
        Skill skill = readSkill();
        return new Move(type, direction, skill);
    }

    /**
     * @return
     * @throws Exception
     * reads a single skill object
     */
    public Skill readSkill() throws Exception {
        int skillId = readInt();
        if(skillId == -1){
            return null;
        }
        int cooldownLeft = readInt();
        return new Skill(skillId, cooldownLeft);
    }

    /**
     * @param data
     * function writes bytes in byte array
     */
    public void write(byte[] data){
        for(Byte b : data) {
            buffer.add(b);
        }
    }

    /**
     * @param value
     * function writes int value as bytes in byte array
     */
    public void write(int value){
        for(Byte b : intToByteArray(value)) {
            buffer.add(b);
        }
    }

    /**
     * @param value
     * function writes float value as bytes in byte array
     */
    public void write(float value){
        for(Byte b : floatToByteArray(value)) {
            buffer.add(b);
        }
    }

    /**
     * @param value
     * function writes string as bytes in byte array
     */
    public void write(String value){
        write(value.length());
        for(Byte b : value.getBytes(StandardCharsets.US_ASCII)) {
            buffer.add(b);
        }
    }

    /**
     * @param move
     * function writes move object as bytes in byte array
     */
    public void write(Move move){
        write(move.getType().getId());
        write(move.getDirection().getId());
        write(move.getSkill());
    }

    /**
     * @param skill
     * function writes skill object as bytes in byte array
     */
    public void write(Skill skill){
        if(skill != null)
            write(skill.getId());
        else
            write(-1);
    }

    /**
     * prepends the length of current data in first 4 indexes of byte array
     */
    public void writeLength(){
        int i = 0;
        for(Byte b : intToByteArray(buffer.size())) {
            buffer.add(i, b);
            i++;
        }
    }

    public List<Byte> getBuffer() {
        return buffer;
    }

    public byte[] toByteArray() {
        final int n = buffer.size();
        byte[] ret = new byte[n];
        for (int i = 0; i < n; i++) {
            ret[i] = buffer.get(i);
        }
        return ret;
    }

    private byte[] intToByteArray(int value){
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putInt(value);
        buf.flip();
        byte[] arr = buf.array();
        return reverse(arr, arr.length);
    }

    private byte[] floatToByteArray(float value){
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putFloat(value);
        buf.flip();
        byte[] arr = buf.array();
        return reverse(arr, arr.length);
    }

    public static byte[] reverse(byte[] a, int n)
    {
        byte[] b = new byte[n];
        int j = n;
        for (int i = 0; i < n; i++) {
            b[j - 1] = a[i];
            j = j - 1;
        }
        return b;
    }

    public static byte[] splitMessage(byte[] message, int start, int end) {
        int size=end-start+1;
        byte[] splitted=new byte[size];
        int counter=0;
        for(int i=start;i<=end;i++){
            splitted [counter] = message[i];
            counter++;

        }
        return splitted;
    }

    public static int convertToInt(byte[] buffer){
        int result=0;
        if(buffer.length==4) {
            result = buffer[0] << 24 | (buffer[1] & 0xFF) << 16 | (buffer[2] & 0xFF) << 8 | (buffer[3] & 0xFF);
        }
        else if (buffer.length==2){
            result=(buffer[0] & 0xFF) << 8 | (buffer[1] & 0xFF);
        }
        else{
            exit(-1);
        }
        return Integer.reverseBytes(result);
    }
}