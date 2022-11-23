package fr.ducouscous.mcscript.mc;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Table;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class GameSettingTable extends Table {
    public GameSettingTable() {
        update();
    }

    public void update() {
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        setValue("keyBindForward", new Variable(new KeybindTable(settings.keyBindForward)));
        setValue("keyBindBack", new Variable(new KeybindTable(settings.keyBindBack)));
        setValue("keyBindLeft", new Variable(new KeybindTable(settings.keyBindLeft)));
        setValue("keyBindRight", new Variable(new KeybindTable(settings.keyBindRight)));
        setValue("keyBindAttack", new Variable(new KeybindTable(settings.keyBindAttack)));
        setValue("keyBindChat", new Variable(new KeybindTable(settings.keyBindChat)));
        setValue("keyBindCommand", new Variable(new KeybindTable(settings.keyBindCommand)));
        setValue("keyBindFullscreen", new Variable(new KeybindTable(settings.keyBindFullscreen)));
        setValue("keyBindDrop", new Variable(new KeybindTable(settings.keyBindDrop)));
        setValue("keyBindInventory", new Variable(new KeybindTable(settings.keyBindInventory)));
        setValue("keyBindPlayerList", new Variable(new KeybindTable(settings.keyBindPlayerList)));
        setValue("keyBindJump", new Variable(new KeybindTable(settings.keyBindJump)));
        setValue("keyBindScreenshot", new Variable(new KeybindTable(settings.keyBindScreenshot)));
        setValue("keyBindSmoothCamera", new Variable(new KeybindTable(settings.keyBindSmoothCamera)));
        setValue("keyBindSprint", new Variable(new KeybindTable(settings.keyBindSprint)));
        setValue("keyBindUse", new Variable(new KeybindTable(settings.keyBindUse)));
        setValue("ofKeyBindZoom", new Variable(new KeybindTable(settings.ofKeyBindZoom)));
        setValue("keyBindTogglePerspective", new Variable(new KeybindTable(settings.keyBindPerspective)));
        setValue("keyBindSpectatorOutlines", new Variable(new KeybindTable(settings.keyBindSpectatorOutlines)));
        setValue("keyBindStreamToggleMic", new Variable(new KeybindTable(settings.keyBindStreamToggleMic)));
    }
}
