package fr.ducouscous.mcscript.builtin.math;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Table;

public class MathTable extends Table {
    public MathTable() {
        getValues().put("min", new Variable(new Min()));
        getValues().put("max", new Variable(new Max()));
        getValues().put("floor", new Variable(new Floor()));
        getValues().put("ceil", new Variable(new Ceil()));
        getValues().put("hypot", new Variable(new Hypot()));
        getValues().put("cosh", new Variable(new Cosh()));
        getValues().put("tanh", new Variable(new Tanh()));
        getValues().put("sinh", new Variable(new Sinh()));
        getValues().put("sin", new Variable(new Sin()));
        getValues().put("cos", new Variable(new Cos()));
        getValues().put("tan", new Variable(new Tan()));
        getValues().put("atan", new Variable(new Atan()));
        getValues().put("acos", new Variable(new Acos()));
        getValues().put("asin", new Variable(new Asin()));
        getValues().put("atan2", new Variable(new Atan2()));
        getValues().put("sqrt", new Variable(new Sqrt()));
        getValues().put("abs", new Variable(new Abs()));
    }
}
