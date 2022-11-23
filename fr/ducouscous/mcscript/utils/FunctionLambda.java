package fr.ducouscous.mcscript.utils;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;

public class FunctionLambda extends Function {
    public interface Lambda {
        Variable run(Variable...arguments);
    }

    private final Lambda lambda;
    public FunctionLambda(Lambda lambda) {
        this.lambda = lambda;
    }

    @Override
    public Variable run(Variable... variables) {
        return lambda.run(variables);
    }
}
