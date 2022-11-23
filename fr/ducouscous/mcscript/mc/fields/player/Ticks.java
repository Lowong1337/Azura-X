package fr.ducouscous.mcscript.mc.fields.player;

import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.mcscript.mc.fields.Field;
import net.minecraft.client.Minecraft;

public class Ticks {
    public static class TicksExisted extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.ticksExisted);
        }

        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.ticksExisted = (int) value.getValue();
        }
    }
    public static class DeathTime extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.deathTime);
        }

        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.deathTime = (int) value.getValue();
        }
    }
    public static class HurtTime extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.hurtTime);
        }

        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.hurtTime = (int) value.getValue();
        }
    }
    public static class SprintingTicksLeft extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.sprintingTicksLeft);
        }

        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.sprintingTicksLeft = (int) value.getValue();
        }
    }
    public static class ArrowHitTimer extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.arrowHitTimer);
        }

        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.arrowHitTimer = (int) value.getValue();
        }
    }
}
