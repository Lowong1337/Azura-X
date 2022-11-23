package fr.ducouscous.mcscript.mc.fields.timer;

import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.mcscript.mc.fields.Field;
import net.minecraft.client.Minecraft;

public class TimerSpeed extends Field<Number> {
    @Override
    protected Number get() {
        return new Number(Minecraft.getMinecraft().timer.timerSpeed);
    }

    @Override
    protected void set(Number number) {
        Minecraft.getMinecraft().timer.timerSpeed = (float) number.getValue();
    }
}
