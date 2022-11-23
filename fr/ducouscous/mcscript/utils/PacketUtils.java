package fr.ducouscous.mcscript.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void sendPacket(Packet<?> packet) {
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
}
