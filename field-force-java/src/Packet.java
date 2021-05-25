import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

enum ServerPackets
{
    GAMEMODE(2),
    TURNORDER(3),
    PLAYERINFORMATION(4),
    GAMEFIELD(5),
    MOVEREQUEST(6),
    MOVEDISTRIBUTION(8),
    NEWGAMESTATE(9),
    ERROR(10),
    GAMEOVER(11);

    private final int id;
    ServerPackets(int id){
        this.id = id;
    }
    public int getId() {
        return id;
    }
}

enum ClientPackets
{
    PLAYERNAME(1),
    MOVEREPLY(7);

    private final int id;
    ClientPackets(int id){
        this.id = id;
    }
    public int getId() {
        return id;
    }
}

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

    public void writeType(int value){
        buffer.add((byte) value);
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
        byte ret[] = new byte[n];
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
        buffer = Packet.reverse(buffer, buffer.length);
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
        return result;
    }
}