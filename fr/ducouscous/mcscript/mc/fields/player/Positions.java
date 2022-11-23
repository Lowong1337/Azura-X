package fr.ducouscous.mcscript.mc.fields.player;

import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.mcscript.mc.fields.Field;
import net.minecraft.client.Minecraft;

public class Positions {
    public static class PositionY extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.posY);
        }
        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.posY = value.getValue();
        }
    }
    public static class PositionX extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.posX);
        }
        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.posX = value.getValue();
        }
    }
    public static class PositionZ extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.posZ);
        }
        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.posZ = value.getValue();
        }
    }
}
