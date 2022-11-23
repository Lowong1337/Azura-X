package fr.ducouscous.mcscript.builtin;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;
import fr.ducouscous.csl.running.variable.values.Table;

public class PushFunction extends Function {
    @Override
    public Variable run(Variable... variables) {
        for (int i = 1; i < variables.length; i++) {
            ((Table) variables[0].getValue()).push(variables[i]);
        }
        return null;
    }
}
