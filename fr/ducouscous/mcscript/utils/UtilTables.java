package fr.ducouscous.mcscript.utils;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Boolean;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.StringVar;
import fr.ducouscous.csl.running.variable.values.Table;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class UtilTables {
    public static class BlockPos extends Table {
        public BlockPos(double x, double y, double z) {
            setValue("posX", new Variable(new Number(x)));
            setValue("posY", new Variable(new Number(y)));
            setValue("posZ", new Variable(new Number(z)));
        }
    }
    public static class Packet extends Table {
        private static final Minecraft mc = Minecraft.getMinecraft();
        public Packet() {
            setValue("C01PacketChatMessage", new Variable(new FunctionLambda((args) -> {
                return new Variable(new PacketTables.C01PacketChatMessage(args[0].getValue().toString()));
            })));
            setValue("C03PacketPlayer", new Variable(new FunctionLambda((args) -> {
                return new Variable(new PacketTables.C03PacketPlayer(
                        ((Boolean) args[0].getValue()).getValue()
                ));
            })));
            setValue("C04PacketPlayerPosition", new Variable(new FunctionLambda((args) -> {
                return new Variable(new PacketTables.C04PacketPlayerPosition(
                        ((Number) args[0].getValue()).getValue(),
                        ((Number) args[1].getValue()).getValue(),
                        ((Number) args[2].getValue()).getValue(),
                        ((Boolean) args[3].getValue()).getValue()
                ));
            })));
            setValue("C05PacketPlayerLook", new Variable(new FunctionLambda((args) -> {
                return new Variable(new PacketTables.C05PacketPlayerLook(
                        (float) ((Number) args[0].getValue()).getValue(),
                        (float) ((Number) args[1].getValue()).getValue(),
                        ((Boolean) args[2].getValue()).getValue()
                ));
            })));
            setValue("C06PacketPlayerPosLook", new Variable(new FunctionLambda((args) -> {
                return new Variable(new PacketTables.C06PacketPlayerPosLook(
                        ((Number) args[0].getValue()).getValue(),
                        ((Number) args[1].getValue()).getValue(),
                        ((Number) args[2].getValue()).getValue(),
                        (float) ((Number) args[3].getValue()).getValue(),
                        (float) ((Number) args[4].getValue()).getValue(),
                        ((Boolean) args[5].getValue()).getValue()
                ));
            })));
            setValue("C07PacketPlayerDigging", new Variable(new FunctionLambda((args) -> {
                return new Variable(new PacketTables.C07PacketPlayerDigging(
                        (int) ((Number) args[0].getValue()).getValue(),
                        ((Table) args[1].getValue()),
                        (int) ((Number) args[2].getValue()).getValue()
                ));
            })));
            setValue("sendPacket", new Variable(new FunctionLambda((args) -> {
                switch (((Table) args[0].getValue()).getValue("type").getValue().toString()) {
                    case "C01PacketChatMessage":
                        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C01PacketChatMessage(
                                ((StringVar) ((Table) args[0].getValue()).getValue("message").getValue()).getValue()
                        ));
                        break;
                    case "C03PacketPlayer":
                        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer(
                                ((Boolean) ((Table) args[0].getValue()).getValue("onGround").getValue()).getValue()
                        ));
                        break;
                    case "C04PacketPlayerPosition":
                        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                                ((Number) ((Table) args[0].getValue()).getValue("posX").getValue()).getValue(),
                                ((Number) ((Table) args[0].getValue()).getValue("posY").getValue()).getValue(),
                                ((Number) ((Table) args[0].getValue()).getValue("posZ").getValue()).getValue(),
                                ((Boolean) ((Table) args[0].getValue()).getValue("onGround").getValue()).getValue()
                        ));
                        break;
                    case "C05PacketPlayerLook":
                        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C05PacketPlayerLook(
                                (float) ((Number) ((Table) args[0].getValue()).getValue("yaw").getValue()).getValue(),
                                (float) ((Number) ((Table) args[0].getValue()).getValue("pitch").getValue()).getValue(),
                                ((Boolean) ((Table) args[0].getValue()).getValue("onGround").getValue()).getValue()
                        ));
                        break;
                    case "C06PacketPlayerPosLook":
                        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(
                                ((Number) ((Table) args[0].getValue()).getValue("posX").getValue()).getValue(),
                                ((Number) ((Table) args[0].getValue()).getValue("posY").getValue()).getValue(),
                                ((Number) ((Table) args[0].getValue()).getValue("posZ").getValue()).getValue(),
                                (float) ((Number) ((Table) args[0].getValue()).getValue("yaw").getValue()).getValue(),
                                (float) ((Number) ((Table) args[0].getValue()).getValue("pitch").getValue()).getValue(),
                                ((Boolean) ((Table) args[0].getValue()).getValue("onGround").getValue()).getValue()
                        ));
                        break;
                    case "C07PacketPlayerDigging":
                        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(
                                C07PacketPlayerDigging.Action.values()[(int) ((Number) ((Table) args[0].getValue()).getValue("action").getValue()).getValue()],
                                new net.minecraft.util.BlockPos(
                                        ((Number) ((Table) args[0].getValue()).getValue("posX").getValue()).getValue(),
                                        ((Number) ((Table) args[0].getValue()).getValue("posY").getValue()).getValue(),
                                        ((Number) ((Table) args[0].getValue()).getValue("posZ").getValue()).getValue()
                                ),
                                EnumFacing.values()[(int) ((Number) ((Table) args[0].getValue()).getValue("facing").getValue()).getValue()]
                        ));
                        break;
                }
                return null;
            })));
        }
    }
    public static class Movement extends Table {
        public Movement() {
            setValue("setMoveSpeed", new Variable(new FunctionLambda((args) -> {
                MovementUtils.setMoveSpeed(((Number) args[0].getValue()).getValue());
                return null;
            })));
            setValue("vPacketClip", new Variable(new FunctionLambda((args) -> {
                MovementUtils.vPacketClip(((Number) args[0].getValue()).getValue(), ((Boolean) args[1].getValue()).getValue());
                return null;
            })));
            setValue("hPacketClip", new Variable(new FunctionLambda((args) -> {
                MovementUtils.hPacketClip(((Number) args[0].getValue()).getValue(), ((Boolean) args[1].getValue()).getValue());
                return null;
            })));
            setValue("getDirection", new Variable(new FunctionLambda((args) -> {
                if (args.length == 0) {
                    return new Variable(new Number(MovementUtils.getDirection()));
                } else {
                    return new Variable(new Number(MovementUtils.getDirection((int) ((Number) args[0].getValue()).getValue(), (int) ((Number) args[1].getValue()).getValue())));
                }
            })));
            setValue("setSpeed", new Variable(new FunctionLambda((args) -> {
                if (args.length == 1) {
                    MovementUtils.setSpeed(((Number) args[0].getValue()).getValue());
                } else {
                    MovementUtils.setSpeed(((Number) args[0].getValue()).getValue(), (float) ((Number) args[1].getValue()).getValue());
                }
                return null;
            })));
            setValue("getSpeed", new Variable(new FunctionLambda((args) -> {
                return new Variable(new Number(MovementUtils.getSpeed()));
            })));
            setValue("getRelativePosition", new Variable(new FunctionLambda((args) -> {
                double[] pos;
                if (args.length == 1) {
                    pos = MovementUtils.getRelativePosition(((Number) args[0].getValue()).getValue());
                } else {
                    pos = MovementUtils.getRelativePosition(((Number) args[0].getValue()).getValue(), (float) ((Number) args[1].getValue()).getValue());
                }
                Table out = new Table();
                out.push(new Variable(new Number(pos[0])));
                out.push(new Variable(new Number(pos[1])));

                return new Variable(out);
            })));
        }
    }
}
