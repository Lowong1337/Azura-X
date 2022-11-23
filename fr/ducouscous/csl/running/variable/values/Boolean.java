package fr.ducouscous.csl.running.variable.values;

import fr.ducouscous.csl.running.variable.VariableValue;

public class Boolean implements VariableValue {
    private final boolean value;
    public Boolean(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }

    @Override
    public Object value() {
        return value;
    }
}
