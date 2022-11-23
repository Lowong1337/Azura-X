package fr.ducouscous.csl.running.variable.values;

import fr.ducouscous.csl.running.variable.VariableValue;

public class Number implements VariableValue {
    private final double value;
    public Number(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value().toString();
    }

    @Override
    public Object value() {
        if (value == (int) value) return (int) value;
        return getValue();
    }
}
