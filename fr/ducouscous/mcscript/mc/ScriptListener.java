package fr.ducouscous.mcscript.mc;

import fr.ducouscous.csl.running.Script;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;
import fr.ducouscous.mcscript.mc.events.Event;

import java.util.ArrayList;
import java.util.List;

public class ScriptListener {
    public MinecraftTable table = new MinecraftTable();
    private static final List<Script> scripts = new ArrayList<>();

    public static List<Script> getScripts() {
        return scripts;
    }

    public void onEvent(Event event) {
        for (Script script : scripts) {
            event.call(script);
        }
    }
}
