package fr.ducouscous.mcscript.mc.fields.player;

import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.mcscript.mc.fields.Field;
import net.minecraft.client.Minecraft;

public class Controller {
    public static class MoveForward extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.moveForward);
        }
        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.moveForward = (float) value.getValue();
        }
    }
    public static class MoveStrafing extends Field<Number> {
        @Override
        protected Number get() {
            return new Number(Minecraft.getMinecraft().thePlayer.moveStrafing);
        }
        @Override
        protected void set(Number value) {
            Minecraft.getMinecraft().thePlayer.moveStrafing = (float) value.getValue();
        }
    }
}
