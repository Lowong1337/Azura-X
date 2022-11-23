package fr.ducouscous.csl.running.variable.values;

import fr.ducouscous.csl.running.variable.VariableValue;

public class StringVar implements VariableValue {
    private final String value;
    public StringVar(Object value) {
        this.value = value+"";
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Object value() {
        return value;
    }
}
