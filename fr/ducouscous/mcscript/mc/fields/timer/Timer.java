package fr.ducouscous.mcscript.mc.fields.timer;

import fr.ducouscous.csl.running.variable.values.Table;
import fr.ducouscous.mcscript.mc.TimerTable;
import fr.ducouscous.mcscript.mc.fields.Field;

public class Timer extends Field<Table> {
    TimerTable table = new TimerTable();
    @Override
    protected Table get() {
        return table;
    }

    @Override
    protected void set(Table table) {

    }
}
