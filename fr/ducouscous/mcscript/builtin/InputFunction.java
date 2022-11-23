package fr.ducouscous.mcscript.builtin;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;
import fr.ducouscous.csl.running.variable.values.StringVar;

import java.util.Scanner;

public class InputFunction extends Function {
    Scanner scanner = new Scanner(System.in);
    @Override
    public Variable run(Variable... variables) {
        return new Variable(new StringVar(scanner.nextLine()));
    }
}
