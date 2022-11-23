package fr.ducouscous.csl.compiler.preprocessor;

import java.util.HashMap;

public class Condition {
    public boolean isValid(HashMap<String, String> define) {
        if (type == Type.UNDEFINED)
            return !define.containsKey(name);
        return define.containsKey(name);
    }

    public enum Type {
        DEFINED,
        UNDEFINED
    }

    @Override
    public String toString() {
        return type + " " + name;
    }

    Type type;
    String name;
    public Condition(String name, Type type) {
        this.name = name;
        this.type = type;
    }
}
