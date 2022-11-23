package fr.ducouscous.mcscript.builtin.math;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;
import fr.ducouscous.csl.running.variable.values.Number;

public class Acos extends Function {
    @Override
    public Variable run(Variable... variables) {
        return new Variable(new Number(Math.acos((double) variables[0].getValue().value())));
    }
}
