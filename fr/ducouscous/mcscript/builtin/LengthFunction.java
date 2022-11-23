package fr.ducouscous.mcscript.builtin;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.Table;

public class LengthFunction extends Function {
    @Override
    public Variable run(Variable... variables) {
        return new Variable(new Number(((Table) variables[0].getValue()).length()));
    }
}
