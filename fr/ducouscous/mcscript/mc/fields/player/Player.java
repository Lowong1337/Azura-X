package fr.ducouscous.mcscript.mc.fields.player;

import fr.ducouscous.csl.running.variable.values.Table;
import fr.ducouscous.mcscript.mc.fields.Field;
import fr.ducouscous.mcscript.mc.player.PlayerTable;

public class Player extends Field<Table> {
    PlayerTable table = new PlayerTable();

    @Override
    protected Table get() {
        return table;
    }

    @Override
    protected void set(Table table) {

    }
}
