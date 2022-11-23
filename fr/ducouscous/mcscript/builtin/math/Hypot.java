package fr.ducouscous.mcscript.builtin.math;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;
import fr.ducouscous.csl.running.variable.values.Number;

public class Hypot extends Function {
    @Override
    public Variable run(Variable... variables) {
        return new Variable(new Number(Math.hypot((double) variables[0].getValue().value(), (double) variables[1].getValue().value())));
    }
}
