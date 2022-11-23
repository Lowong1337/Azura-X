package fr.ducouscous.mcscript.mc.fields.timer;

import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.mcscript.mc.fields.Field;
import net.minecraft.client.Minecraft;

public class ElapsedPartialTicks extends Field<Number> {
    @Override
    protected Number get() {
        return new Number(Minecraft.getMinecraft().timer.elapsedPartialTicks);
    }

    @Override
    protected void set(Number number) {
        Minecraft.getMinecraft().timer.elapsedPartialTicks = (int) number.getValue();
    }
}
