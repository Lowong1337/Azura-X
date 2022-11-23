package fr.ducouscous.mcscript.mc.player;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Boolean;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.Table;
import fr.ducouscous.mcscript.mc.fields.player.*;
import fr.ducouscous.mcscript.utils.FunctionLambda;
import net.minecraft.client.Minecraft;

public class PlayerTable extends Table {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public PlayerTable() {
        setValue("motionX", new Motions.MotionX());
        setValue("motionY", new Motions.MotionY());
        setValue("motionZ", new Motions.MotionZ());

        setValue("posX", new Positions.PositionX());
        setValue("posY", new Positions.PositionY());
        setValue("posZ", new Positions.PositionZ());

        setValue("isCollided", new Collisions.Collided());
        setValue("isCollidedHorizontally", new Collisions.CollidedHorizontally());
        setValue("isCollidedVertically", new Collisions.CollidedVertically());
        setValue("onGround", new Collisions.OnGround());
        setValue("noClip", new Collisions.NoClip());
        setValue("isInWeb", new Collisions.InWeb());

        setValue("moveForward", new Controller.MoveForward());
        setValue("moveStrafing", new Controller.MoveStrafing());

        setValue("fallDistance", new Movement.FallDistance());
        setValue("jumpMovementFactor", new Movement.JumpMovementFactor());
        setValue("speedInAir", new Movement.SpeedInAir());

        setValue("isDead", new Other.Dead());
        setValue("dimension", new Other.Dimension());
        setValue("forceSpawn", new Other.ForceSpawn());
        setValue("isSwingInProgress", new Other.IsSwingInProgress());
        setValue("isAirBorne", new Other.IsAirBorne());

        setValue("cameraPitch", new Rotations.CameraPitch());
        setValue("cameraYaw", new Rotations.CameraPitch());
        setValue("rotationYaw", new Rotations.Yaw());
        setValue("rotationPitch", new Rotations.Pitch());

        setValue("arrowHitTimer", new Ticks.ArrowHitTimer());
        setValue("deathTime", new Ticks.DeathTime());
        setValue("ticksExisted", new Ticks.TicksExisted());
        setValue("sprintingTicksLeft", new Ticks.SprintingTicksLeft());
        setValue("hurtTime", new Ticks.HurtTime());

        setValue("jump", new Variable(new FunctionLambda((args) -> {
            mc.thePlayer.jump();
            return null;
        })));

        setValue("isOnLadder", new Variable(new FunctionLambda((args) -> {
            return new Variable(new Boolean(mc.thePlayer.isOnLadder()));
        })));

        setValue("setPosition", new Variable(new FunctionLambda((args) -> {
            mc.thePlayer.setPosition(
                    ((Number) args[0].getValue()).getValue(),
                    ((Number) args[1].getValue()).getValue(),
                    ((Number) args[2].getValue()).getValue()
            );
            return null;
        })));
    }
}
