//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package fr.ducouscous.mcscript.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

@SuppressWarnings("unused")
public class MovementUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void vPacketClip(double v, boolean ground) {
        PacketUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + v, mc.thePlayer.posZ, ground));
    }

    public static double getSpeed() {
        return Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }

    public static void hPacketClip(double h, boolean ground) {
        double[] pos = getRelativePosition(h);
        PacketUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + pos[0], mc.thePlayer.posY, mc.thePlayer.posZ + pos[1], ground));
    }

    public static float getDirection() {
        return getDirection(Float.compare(0.0F, mc.thePlayer.moveStrafing), Float.compare(mc.thePlayer.moveForward, 0.0F));
    }

    public static boolean isMoving() {
        return mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F;
    }

    public static float getDirection(int x, int y) {
        return mc.thePlayer.rotationYaw + (float)(x * 90) + (float)(90 * y * y - y * 90) + (float)(y * x * 135) + (float)(y * (y + 1) / 2 * 180 * x * x);
    }

    public static void setMoveSpeed(double speed) {
        if (isMoving()) {
            setSpeed(speed, getDirection());
        } else {
            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0.0;
        }

    }

    public static void setSpeed(double speed) {
        double[] pos = getRelativePosition(speed);
        mc.thePlayer.motionX = pos[0];
        mc.thePlayer.motionZ = pos[1];
    }

    public static void setSpeed(double speed, float yaw) {
        double[] pos = getRelativePosition(speed, yaw);
        mc.thePlayer.motionX = pos[0];
        mc.thePlayer.motionZ = pos[1];
    }

    public static double[] getRelativePosition(double distance) {
        return getRelativePosition(distance, mc.thePlayer.rotationYaw);
    }

    public static double[] getRelativePosition(double distance, float yaw) {
        double angle = Math.toRadians(yaw);
        return new double[]{-Math.sin(angle) * distance, Math.cos(angle) * distance};
    }
}
