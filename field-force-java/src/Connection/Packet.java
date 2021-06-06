package Connection;

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

    public Packet(int id) {
        buffer = new ArrayList<>();
        readPos = 0;

        write(id);
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

    public List<Player> readPlayers() throws Exception {
        List<Player> players = new ArrayList<>();
        int playerCount = readInt();
        for (int i = 0; i < playerCount; i++){
            int playerNumber = readInt();
            String playerName = readString();
            int xPos = readInt();
            int yPos = readInt();
            players.add(new Player((char)(playerNumber+'0'), playerNumber, playerName, xPos, yPos));
        }
        return players;
    }

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

    public Move readMove() throws Exception {
        MoveType type =  MoveType.fromInt(readInt());
        Direction direction = Direction.fromInt(readInt());
        Skill skill = readSkill();
        return new Move(type, direction, skill);
    }

    public Skill readSkill() throws Exception {
        int skillId = readInt();
        return new Skill(skillId);
    }

    public void write(byte[] data){
        for(Byte b : data) {
            buffer.add(b);
        }
    }

    public void write(int value){
        for(Byte b : intToByteArray(value)) {
            buffer.add(b);
        }
    }

    public void write(float value){
        for(Byte b : floatToByteArray(value)) {
            buffer.add(b);
        }
    }

    public void write(String value){
        write(value.length());
        for(Byte b : value.getBytes(StandardCharsets.US_ASCII)) {
            buffer.add(b);
        }
    }

    public void write(Move move){
        write(move.getType().getId());
        write(move.getDirection().getId());
        write(move.getSkill());
    }

    public void write(Skill skill){
        if(skill != null)
            write(skill.getId());
        else
            write(0);
    }

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