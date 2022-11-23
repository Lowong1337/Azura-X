package fr.ducouscous.mcscript.mc.events;

import fr.ducouscous.csl.running.Script;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;
import fr.ducouscous.mcscript.mc.MinecraftTable;

public class UpdateEvent implements Event {
    @Override
    public void call(Script script) {
        Variable variable = script.getGlobalValue("onUpdate");
        if (variable != null) {
            Function update = (Function) variable.getValue();

            update.run();
        }
    }
}
