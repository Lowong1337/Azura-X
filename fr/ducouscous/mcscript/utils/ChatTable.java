package fr.ducouscous.mcscript.utils;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Table;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatTable extends Table {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public ChatTable() {
        setValue("print", new Variable(new FunctionLambda((args) -> {
            mc.thePlayer.addChatComponentMessage(new ChatComponentText(args[0].toString()));
            return null;
        })));
        setValue("send", new Variable(new FunctionLambda((args) -> {
            mc.thePlayer.sendChatMessage(args[0].toString());
            return null;
        })));
    }
}
