package fr.ducouscous.csl.running.variable;

public class Variable {
    private VariableValue value;

    public Variable(VariableValue value) {
        this.value = value;
    }

    public VariableValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public void setValue(VariableValue value) {
        this.value = value;
    }
}
