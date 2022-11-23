package fr.ducouscous.mcscript.mc.events;

import fr.ducouscous.csl.running.Script;
import fr.ducouscous.mcscript.mc.MinecraftTable;

public interface Event {
    void call(Script script);
}
