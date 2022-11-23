package fr.ducouscous.csl.running;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.VariableValue;
import fr.ducouscous.csl.running.variable.values.Function;
import fr.ducouscous.csl.running.variable.values.Table;

public class Script {
    private final Variable global;
    private final Table globalTable;
    private Function globalFunction;

    public Table getGlobalTable() {
        return globalTable;
    }

    public void load() {
        globalTable.getValues().forEach((k, v) -> {
            if (v.getValue() instanceof Function) {
                ((Function) v.getValue()).script = this;
            }
        });
        globalFunction.script = this;
    }

    public void setGlobalFunction(Function globalFunction) {
        this.globalFunction = globalFunction;
    }

    public Function getGlobalFunction() {
        return globalFunction;
    }

    public Variable getGlobal() {
        return global;
    }

    public void main() {
        getGlobalFunction().run();
    }

    public void setGlobalValue(String name, Variable value) {
        globalTable.setValue(name, value);
    }
    public void setGlobalValue(String name, VariableValue value) {
        globalTable.setValue(name, new Variable(value));
    }

    public Variable getGlobalValue(String name) {
        return globalTable.getValue(name);
    }

    public Script() {
        globalTable = new Table();
        global = new Variable(globalTable);
    }
}
