package Connection;

import AI.AI;
import Game.Move;
import Game.MoveType;
import Game.SkillType;

public class ClientSend {

    private static void sendPacket(Packet packet){
        packet.writeLength();
        Client.getInstance().sendPacket(packet);
    }

    public static void sendPlayername(){
        Packet packet = new Packet();
        packet.write(ClientPackets.PLAYERNAME.getId());
        packet.write(AI.playername);
        packet.write(AI.skill1);
        packet.write(AI.skill2);
        sendPacket(packet);
    }

    public static void sendMovereply(int id, Move move){
        Packet packet = new Packet();
        packet.write(ClientPackets.MOVEREPLY.getId());
        packet.write(id);
        packet.write(move);
        if(move.getType() == MoveType.SKILL) {
            move.getSkill().setUsed();
            if(move.getSkill().getId() == AI.skill1)
                AI.ownPlayer.getSkill1().setUsed();
            if(move.getSkill().getId() == AI.skill2)
                AI.ownPlayer.getSkill2().setUsed();
        }
        sendPacket(packet);
    }
}
