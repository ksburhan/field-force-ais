import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    public Packet(int _id) {
        buffer = new ArrayList<>();
        readPos = 0;

        write(_id);
    }

    public Packet(byte[] _data) {
        buffer = new ArrayList<Byte>();
        readPos = 0;

        //setBytes(_data);
    }

    public void write(int value){
        for(Byte b : intToByteArray(value)) {
            buffer.add(b);
        }
    }

    public void writeType(int value){
        buffer.add((byte) value);
    }

    public void write(String value){
        write(value.length());
        for(Byte b : value.getBytes(StandardCharsets.US_ASCII)) {
            buffer.add(b);
        }
    }

    public void writeLength(){
        //buffer.add(0,(byte) buffer.size());
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

    static byte[] reverse(byte[] a, int n)
    {
        byte[] b = new byte[n];
        int j = n;
        for (int i = 0; i < n; i++) {
            b[j - 1] = a[i];
            j = j - 1;
        }
        return b;
    }
}