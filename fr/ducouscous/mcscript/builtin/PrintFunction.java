package fr.ducouscous.mcscript.builtin;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;

public class PrintFunction extends Function {
    @Override
    public Variable run(Variable... variables) {
        System.out.println(variables[0].getValue());
        return null;
    }
}
