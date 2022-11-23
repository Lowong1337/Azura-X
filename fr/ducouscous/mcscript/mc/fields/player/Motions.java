package fr.ducouscous.mcscript.mc.fields.player;

import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.mcscript.mc.fields.Field;
import net.minecraft.client.Minecraft;

public class Motions {
    public static class MotionY extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.motionY);
        }
        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.motionY = value.getValue();
        }
    }
    public static class MotionX extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.motionX);
        }
        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.motionX = value.getValue();
        }
    }
    public static class MotionZ extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.motionZ);
        }
        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.motionZ = value.getValue();
        }
    }
}
