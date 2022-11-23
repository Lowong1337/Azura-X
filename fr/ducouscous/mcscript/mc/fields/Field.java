package fr.ducouscous.mcscript.mc.fields;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.VariableValue;

public abstract class Field<T extends VariableValue> extends Variable {
    public Field() {
        super(null);
    }

    protected abstract T get();
    protected abstract void set(T t);

    @Override
    public VariableValue getValue() {
        return get();
    }

    @Override
    public String toString() {
        return get().toString();
    }

    @Override
    public void setValue(VariableValue value) {
        set((T) value);
    }
}
