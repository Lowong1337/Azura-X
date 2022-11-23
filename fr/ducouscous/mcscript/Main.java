package fr.ducouscous.mcscript;

import fr.ducouscous.csl.compiler.Compiler;
import fr.ducouscous.csl.running.Script;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.Table;

import java.io.File;
import java.io.IOException;

public class Main {
    public static class Print extends Function {
        @Override
        public Variable run(Variable... variables) {
            System.out.println(variables[0]);
            return null;
        }
    }

    public static class Length extends Function {
        @Override
        public Variable run(Variable... variables) {
            return new Variable(new Number(((Table) variables[0].getValue()).length()));
        }
    }

    public static void main(String[] args) throws IOException {
        ScriptRegisterer registerer = new ScriptRegisterer();
        Compiler compiler = new Compiler();
        File out = new File("script.casl");
        Script script = compiler.compile(new File("script.asl"));
        compiler.write(out, script);
        script.setGlobalValue("print", new Print());
        registerer.register(script);
        script.load();
        script.main();
    }
}