package fr.ducouscous.mcscript.utils;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Boolean;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.StringVar;
import fr.ducouscous.csl.running.variable.values.Table;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public class PacketTables {
    public static abstract class Packet extends Table {
        public Packet() {
            setValue("type", new Variable(new StringVar(packetName())));
        }

        abstract String packetName();
    }
    public static class C01PacketChatMessage extends Packet {
        public C01PacketChatMessage(String message) {
            setValue("message", new Variable(new StringVar(message)));
        }
        @Override
        String packetName() {
            return "C01PacketChatMessage";
        }
    }
    public static class C07PacketPlayerDigging extends Packet {
        public C07PacketPlayerDigging(int action, Table pos, int facing) {
            setValue("action", new Variable(new Number(action)));
            setValue("posX", pos.getValue("posX"));
            setValue("posY", pos.getValue("posY"));
            setValue("posZ", pos.getValue("posZ"));
            setValue("facing", new Variable(new Number(facing)));
        }
        @Override
        String packetName() {
            return "C07PacketPlayerDigging";
        }
    }
    public static class C03PacketPlayer extends Packet {
        public C03PacketPlayer(boolean onGround) {
            setValue("onGround", new Variable(new Boolean(onGround)));
        }
        @Override
        String packetName() {
            return "C03PacketPlayer";
        }
    }
    public static class C04PacketPlayerPosition extends C03PacketPlayer {
        public C04PacketPlayerPosition(double x, double y, double z, boolean onGround) {
            super(onGround);
            setValue("posX", new Variable(new Number(x)));
            setValue("posY", new Variable(new Number(y)));
            setValue("posZ", new Variable(new Number(z)));
        }
        @Override
        String packetName() {
            return "C04PacketPlayerPosition";
        }
    }
    public static class C06PacketPlayerPosLook extends C03PacketPlayer {
        public C06PacketPlayerPosLook(double x, double y, double z, float yaw, float pitch, boolean onGround) {
            super(onGround);
            setValue("posX", new Variable(new Number(x)));
            setValue("posY", new Variable(new Number(y)));
            setValue("posZ", new Variable(new Number(z)));
            setValue("yaw", new Variable(new Number(yaw)));
            setValue("pitch", new Variable(new Number(pitch)));
        }
        @Override
        String packetName() {
            return "C06PacketPlayerPosLook";
        }
    }
    public static class C05PacketPlayerLook extends C03PacketPlayer {
        public C05PacketPlayerLook(float yaw, float pitch, boolean onGround) {
            super(onGround);
            setValue("yaw", new Variable(new Number(yaw)));
            setValue("pitch", new Variable(new Number(pitch)));
        }
        @Override
        String packetName() {
            return "C05PacketPlayerLook";
        }
    }
}
