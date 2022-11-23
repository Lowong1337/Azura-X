package fr.ducouscous.mcscript.mc.fields.player;

import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.mcscript.mc.fields.Field;
import net.minecraft.client.Minecraft;

public class Movement {
    public static class SpeedInAir extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.speedInAir);
        }

        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.speedInAir = (float) value.getValue();
        }
    }
    public static class JumpMovementFactor extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.jumpMovementFactor);
        }

        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.jumpMovementFactor = (float) value.getValue();
        }
    }
    public static class FallDistance extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.fallDistance);
        }

        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.fallDistance = (float) value.getValue();
        }
    }
}
