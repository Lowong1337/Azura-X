package fr.ducouscous.mcscript;

import fr.ducouscous.csl.running.Script;
import fr.ducouscous.mcscript.builtin.*;
import fr.ducouscous.mcscript.builtin.math.MathTable;
import fr.ducouscous.mcscript.mc.MinecraftTable;
import fr.ducouscous.mcscript.utils.ChatTable;
import fr.ducouscous.mcscript.utils.UtilTables;

@SuppressWarnings("unused")
public class ScriptRegisterer {
    private static final MathTable math = new MathTable();
    private static final UtilTables.Movement movement = new UtilTables.Movement();
    private static final UtilTables.Packet packet = new UtilTables.Packet();
    private static final ChatTable chat = new ChatTable();

    public void register(Script script) {
        script.setGlobalValue("math", math);

        script.setGlobalValue("Movement", movement);
        script.setGlobalValue("Packet", packet);
        script.setGlobalValue("Chat", chat);

        script.setGlobalValue("print", new PrintFunction());
        script.setGlobalValue("input", new InputFunction());
        script.setGlobalValue("length", new LengthFunction());
        script.setGlobalValue("push", new PushFunction());
        script.setGlobalValue("toString", new ToStringFunction());

        MinecraftTable mc = new MinecraftTable();
        script.setGlobalValue("mc", mc);
    }
}
