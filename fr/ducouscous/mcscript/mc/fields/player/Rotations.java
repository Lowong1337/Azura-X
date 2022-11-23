package fr.ducouscous.mcscript.mc.fields.player;

import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.mcscript.mc.fields.Field;
import net.minecraft.client.Minecraft;

public class Rotations {
    public static class Yaw extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.rotationYaw);
        }

        @Override
        protected void set(Number number) {
            Minecraft.getMinecraft().thePlayer.rotationYaw = (float) number.getValue();
        }
    }
    public static class Pitch extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.rotationPitch);
        }

        @Override
        protected void set(Number number) {
            Minecraft.getMinecraft().thePlayer.rotationPitch = (float) number.getValue();
        }
    }
    public static class CameraPitch extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.cameraPitch);
        }

        @Override
        protected void set(Number number) {
            Minecraft.getMinecraft().thePlayer.cameraPitch = (float) number.getValue();
        }
    }
    public static class CameraYaw extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.cameraYaw);
        }

        @Override
        protected void set(Number number) {
            Minecraft.getMinecraft().thePlayer.cameraYaw = (float) number.getValue();
        }
    }
}
