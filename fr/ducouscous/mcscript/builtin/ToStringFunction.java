package fr.ducouscous.mcscript.builtin;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;
import fr.ducouscous.csl.running.variable.values.StringVar;

public class ToStringFunction extends Function {
    @Override
    public Variable run(Variable... variables) {
        return new Variable(new StringVar(variables[0].getValue().toString()));
    }
}
