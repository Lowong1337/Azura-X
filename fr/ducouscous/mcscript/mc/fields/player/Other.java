package fr.ducouscous.mcscript.mc.fields.player;

import fr.ducouscous.csl.running.variable.values.Boolean;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.mcscript.mc.fields.Field;
import net.minecraft.client.Minecraft;

public class Other {
    public static class Dead extends Field<Boolean> {
        @Override
        protected Boolean get() {
            return new Boolean(Minecraft.getMinecraft().thePlayer.isDead);
        }

        @Override
        protected void set(Boolean value) {
            Minecraft.getMinecraft().thePlayer.isDead = value.getValue();
        }
    }
    public static class IsAirBorne extends Field<Boolean> {
        @Override
        protected Boolean get() {
            return new Boolean(Minecraft.getMinecraft().thePlayer.isAirBorne);
        }

        @Override
        protected void set(Boolean value) {
            Minecraft.getMinecraft().thePlayer.isAirBorne = value.getValue();
        }
    }
    public static class ForceSpawn extends Field<Boolean> {
        @Override
        protected Boolean get() {
            return new Boolean(Minecraft.getMinecraft().thePlayer.forceSpawn);
        }

        @Override
        protected void set(Boolean value) {
            Minecraft.getMinecraft().thePlayer.forceSpawn = value.getValue();
        }
    }
    public static class Dimension extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.dimension);
        }

        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.dimension = (int) value.getValue();
        }
    }
    public static class IsSwingInProgress extends Field<Boolean> {
        @Override
        protected Boolean get() {
            return new Boolean(Minecraft.getMinecraft().thePlayer.isSwingInProgress);
        }

        @Override
        protected void set(Boolean value) {
            Minecraft.getMinecraft().thePlayer.isSwingInProgress = value.getValue();
        }
    }
}
