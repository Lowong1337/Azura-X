package fr.ducouscous.mcscript.mc.functions;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;
import net.minecraft.client.Minecraft;

public class JumpFunction extends Function {
    @Override
    public Variable run(Variable... variables) {
        Minecraft.getMinecraft().thePlayer.jump();
        return null;
    }
}
