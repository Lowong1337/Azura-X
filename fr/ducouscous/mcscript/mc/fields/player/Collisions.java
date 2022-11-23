package fr.ducouscous.mcscript.mc.fields.player;

import fr.ducouscous.csl.running.variable.values.Boolean;
import fr.ducouscous.mcscript.mc.fields.Field;
import net.minecraft.client.Minecraft;

public class Collisions {
    public static class Collided extends Field<Boolean> {
        @Override
        protected Boolean get() {
            return new Boolean(Minecraft.getMinecraft().thePlayer.isCollided);
        }
        @Override
        protected void set(Boolean value) {
            Minecraft.getMinecraft().thePlayer.isCollided = value.getValue();
        }
    }
    public static class CollidedHorizontally extends Field<Boolean> {
        @Override
        protected Boolean get() {
            return new Boolean(Minecraft.getMinecraft().thePlayer.isCollidedHorizontally);
        }
        @Override
        protected void set(Boolean value) {
            Minecraft.getMinecraft().thePlayer.isCollidedHorizontally = value.getValue();
        }
    }
    public static class CollidedVertically extends Field<Boolean> {
        @Override
        protected Boolean get() {
            return new Boolean(Minecraft.getMinecraft().thePlayer.isCollidedVertically);
        }
        @Override
        protected void set(Boolean value) {
            Minecraft.getMinecraft().thePlayer.isCollidedVertically = value.getValue();
        }
    }
    public static class OnGround extends Field<Boolean> {
        @Override
        protected Boolean get() {
            return new Boolean(Minecraft.getMinecraft().thePlayer.onGround);
        }
        @Override
        protected void set(Boolean value) {
            Minecraft.getMinecraft().thePlayer.onGround = value.getValue();
        }
    }
    public static class NoClip extends Field<Boolean> {
        @Override
        protected Boolean get() {
            return new Boolean(Minecraft.getMinecraft().thePlayer.noClip);
        }
        @Override
        protected void set(Boolean value) {
            Minecraft.getMinecraft().thePlayer.noClip = value.getValue();
        }
    }
    public static class InWeb extends Field<Boolean> {
        @Override
        protected Boolean get() {
            return new Boolean(Minecraft.getMinecraft().thePlayer.isInWeb);
        }
        @Override
        protected void set(Boolean value) {
            Minecraft.getMinecraft().thePlayer.isInWeb = value.getValue();
        }
    }
}
