package fr.ducouscous.csl.running.variable.values;

import fr.ducouscous.csl.util.StringUtil;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.VariableValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Table implements VariableValue {
    private final HashMap<String, Variable> values = new HashMap<>();
    private final List<Variable> array = new ArrayList<>();

    public HashMap<String, Variable> getValues() {
        return values;
    }
    public Variable getValue(Object index) {
        return values.get(index.toString());
    }
    public void setValue(Object index, Variable value) {
        values.put(index.toString(), value);
    }

    @Override
    public Object value() {
        return values;
    }

    @Override
    public String toString() {
        return StringUtil.tableToString(this);
    }

    public Variable getValue(int index) {
        return array.get(index);
    }
    public void setValue(int index, Variable variable) {
        array.set(index, variable);
    }

    public void push(Variable variable) {
        array.add(variable);
    }

    public int size() {
        return values.size();
    }

    public int length() {
        return array.size();
    }
}
