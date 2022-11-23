package fr.ducouscous.csl.running.context;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Context {
    private final List<Variable> stack = new ArrayList<>();
    private final Variable[] locals;

    public Context(int maxLocal) {
        this.locals = new Variable[maxLocal];
    }

    public void store(Variable variable, int i) {
        locals[i] = variable;
    }
    public Variable load(int i) {
        return locals[i];
    }
    public void dup() {
        stack.add(stack.get(stack.size()-1));
    }
    public void clear() {
        stack.clear();
    }
    public Variable last() {
        return stack.get(stack.size()-1);
    }
    public Variable last(int offset) {
        return stack.get(stack.size()-1-offset);
    }
    public void push(Variable variable) {
        stack.add(variable);
    }
    public void setLast(Variable variable) {
        stack.set(stack.size()-1, variable);
    }
    public void pop() {
        stack.remove(stack.size()-1);
    }
    public void pop(int offset) {
        stack.remove(stack.size()-1-offset);
    }
    public void pop(int offset, int amount) {
        for (int i = 0; i < amount; i++) {
            stack.remove(stack.size()-offset-1);
        }
    }
    public Variable[] get(int from, int to) {
        return stack.subList(stack.size()-from, stack.size()-to).toArray(new Variable[0]);
    }

    public List<Variable> getStack() {
        return stack;
    }

    public void printStack() {
        System.out.println(Arrays.toString(stack.toArray()));
    }

    public int size() {
        return locals.length;
    }
}
