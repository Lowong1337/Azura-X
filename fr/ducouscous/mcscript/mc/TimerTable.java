package fr.ducouscous.mcscript.mc;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.Table;
import fr.ducouscous.mcscript.mc.fields.timer.ElapsedPartialTicks;
import fr.ducouscous.mcscript.mc.fields.timer.ElapsedTicks;
import fr.ducouscous.mcscript.mc.fields.timer.RenderPartialTicks;
import fr.ducouscous.mcscript.mc.fields.timer.TimerSpeed;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;

public class TimerTable extends Table {
    public TimerTable() {
        setValue("timerSpeed", new TimerSpeed());
        setValue("elapsedPartialTicks", new ElapsedPartialTicks());
        setValue("elapsedTicks", new ElapsedTicks());
        setValue("renderPartialTicks", new RenderPartialTicks());
    }
}
