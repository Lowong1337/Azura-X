package fr.ducouscous.mcscript.mc;

import fr.ducouscous.csl.running.variable.values.Table;
import fr.ducouscous.mcscript.mc.fields.player.Player;
import fr.ducouscous.mcscript.mc.fields.timer.Timer;

public class MinecraftTable extends Table {
    public MinecraftTable() {
        setValue("thePlayer", new Player());
        setValue("timer", new Timer());
    }
}
