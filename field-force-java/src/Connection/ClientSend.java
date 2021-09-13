package Connection;

import AI.AI;
import Game.Move;
import Game.MoveType;
import Game.SkillType;

public class ClientSend {


    /**
     * @param packet
     * writes length of packet for appropriate form and sends packet to server
     */
    private static void sendPacket(Packet packet){
        packet.writeLength();
        Client.getInstance().sendPacket(packet);
    }

    /**
     * sends Pakettype 1. tells server that client ist playing and what name and skills were chosen
     */
    public static void sendPlayername(){
        Packet packet = new Packet();
        packet.write(ClientPackets.PLAYERNAME.getId());
        packet.write(AI.playername);
        packet.write(AI.skill1);
        packet.write(AI.skill2);
        sendPacket(packet);
    }

    /**
     * sends Pakettype 6. tells server the movereply and sets skills on cooldown if used
     */
    public static void sendMovereply(int id, Move move){
        Packet packet = new Packet();
        packet.write(ClientPackets.MOVEREPLY.getId());
        packet.write(id);
        packet.write(move);
        if(move.getType() == MoveType.SKILL) {
            move.getSkill().setOnCooldown();
            if(move.getSkill().getId() == AI.skill1)
                AI.ownPlayer.getSkill1().setOnCooldown();
            if(move.getSkill().getId() == AI.skill2)
                AI.ownPlayer.getSkill2().setOnCooldown();
        }
        sendPacket(packet);
    }
}
