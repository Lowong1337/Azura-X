package fr.ducouscous.csl.running.variable.values;

import fr.ducouscous.csl.running.Instruction;
import fr.ducouscous.csl.running.Script;
import fr.ducouscous.csl.running.context.Context;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.VariableValue;

import java.util.*;

public class Function implements VariableValue {
    private final Instruction[] instructions;
    private final HashMap<String, Integer> labels = new HashMap<>();
    public Script script;

    public Instruction[] getInstructions() {
        return instructions;
    }

    public Function(Instruction...instructions) {
        //System.out.println(Arrays.toString(instructions));
        List<Instruction> i = new ArrayList<>();

        int ind = 0;
        for (Instruction instruction : instructions) {
            if (instruction.getIndex() == Instruction.LABEL) {
                labels.put(instruction.getData(0).toString(), ind);
            }
            i.add(instruction);
            ind++;
        }
        this.instructions = i.toArray(new Instruction[0]);
    }

    public Variable run(Variable...variables) {
        Context context = new Context(5);
        for (int i = 0; i < instructions.length; i++) {
            Variable calc;
            switch (instructions[i].getIndex()) {
                case Instruction.GOTO:
                    i = labels.get(instructions[i].getData(0).toString());
                    break;
                case Instruction.GET:
                    context.setLast(((Table) context.last().getValue()).getValue(instructions[i].getData(0)));
                    break;
                case Instruction.SET:
                    context.last().setValue(context.last(1).getValue());
                    context.pop();
                    context.pop();
                    break;
                case Instruction.INCREMENT:
                    context.last().setValue(new Number(((Number) context.last().getValue()).getValue() + 1));
                    break;
                case Instruction.DECREMENT:
                    context.last().setValue(new Number(((Number) context.last().getValue()).getValue() - 1));
                    break;
                case Instruction.TABLE_SET:
                    ((Table) context.last(1).getValue()).setValue(instructions[i].getData(0), context.last());
                    context.pop();
                    break;
                case Instruction.TABLE_GET:
                    if (context.last().getValue().value() instanceof Integer) {
                        calc = ((Table) context.last(1).getValue()).getValue((int) context.last().getValue().value());
                    } else {
                        calc = ((Table) context.last(1).getValue()).getValue(context.last().getValue().value());
                    }
                    context.pop();
                    context.setLast(calc);
                    break;
                case Instruction.CALL:
                    Variable result = ((Function) context.last().getValue()).run(context.get((int) instructions[i].getData(0)+1, 1));
                    context.pop();
                    context.pop(0, (int) instructions[i].getData(0));
                    context.push(result);
                    break;
                case Instruction.GLOBAL:
                    context.push(script.getGlobal());
                    break;
                case Instruction.STORE:
                    context.store(context.last(), (int) instructions[i].getData(0));
                    context.pop();
                    break;
                case Instruction.LOAD:
                    context.push(context.load((int) instructions[i].getData(0)));
                    break;
                case Instruction.ARG:
                    context.push(variables[(int) instructions[i].getData(0)]);
                    break;
                case Instruction.DUP:
                    context.dup();
                    break;
                case Instruction.LDC:
                    switch ((int) instructions[i].getData(0)) {
                        case Instruction.LDC_STRING:
                            context.push(new Variable(new StringVar(instructions[i].getData(1))));
                            break;
                        case Instruction.LDC_TABLE:
                            context.push(new Variable(new Table()));
                            break;
                        case Instruction.LDC_NUMBER:
                            context.push(new Variable(new Number((double) instructions[i].getData(1))));
                            break;
                        case Instruction.LDC_BOOL:
                            context.push(new Variable(new Boolean((boolean) instructions[i].getData(1))));
                            break;
                    }
                    break;
                case Instruction.RETURN:
                    return context.last();
                case Instruction.ADD:
                    calc = new Variable(new Number(((Number) context.last().getValue()).getValue() + ((Number) context.last(1).getValue()).getValue()));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.MODULO:
                    calc = new Variable(new Number(((Number) context.last().getValue()).getValue() % ((Number) context.last(1).getValue()).getValue()));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.CONCAT:
                    calc = new Variable(new StringVar(context.last().getValue().toString().concat(context.last(1).getValue().toString())));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.SUBTRACT:
                    calc = new Variable(new Number(((Number) context.last().getValue()).getValue() - ((Number) context.last(1).getValue()).getValue()));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.MULTIPLY:
                    calc = new Variable(new Number(((Number) context.last().getValue()).getValue() * ((Number) context.last(1).getValue()).getValue()));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.DIVIDE:
                    calc = new Variable(new Number(((Number) context.last().getValue()).getValue() / ((Number) context.last(1).getValue()).getValue()));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.I_XOR:
                    calc = new Variable(new Number(((int) context.last().getValue().value()) ^ ((int) context.last(1).getValue().value())));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.I_OR:
                    calc = new Variable(new Number(((int) context.last().getValue().value()) | ((int) context.last(1).getValue().value())));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.I_AND:
                    calc = new Variable(new Number(((int) context.last().getValue().value()) & ((int) context.last(1).getValue().value())));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.BITWISE_RIGHT:
                    calc = new Variable(new Number(((int) context.last().getValue().value()) >> ((int) context.last(1).getValue().value())));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.BITWISE_LEFT:
                    calc = new Variable(new Number(((int) context.last().getValue().value()) << ((int) context.last(1).getValue().value())));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.POP:
                    context.pop();
                    break;
                case Instruction.NOT:
                    context.setLast(new Variable(new Boolean(!((Boolean) context.last().getValue()).getValue())));
                    break;
                case Instruction.AND:
                    calc = new Variable(new Boolean(((Boolean) context.last().getValue()).getValue() && ((Boolean) context.last(1).getValue()).getValue()));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.OR:
                    calc = new Variable(new Boolean(((Boolean) context.last().getValue()).getValue() || ((Boolean) context.last(1).getValue()).getValue()));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.IF:
                    if (((Boolean) context.last().getValue()).getValue()) {
                        i = labels.get(instructions[i].getData(0).toString());
                    }
                    context.pop();
                    break;
                case Instruction.EQUALS:
                    calc = new Variable(new Boolean(context.last().getValue().value().equals(context.last(1).getValue().value())));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.NULL:
                    context.push(new Variable(() -> null));
                    break;
                case Instruction.ARRAY_PUSH:
                    ((Table) context.last(1).getValue()).push(context.last());
                    context.pop();
                    break;
                case Instruction.ARRAY_GET:
                    context.setLast(((Table) context.last(1).getValue()).getValue((int) context.last().getValue().value()));
                    context.pop(1);
                    break;
                case Instruction.ARRAY_SET:
                    ((Table) context.last(1).getValue()).setValue((int) instructions[i].getData(0), context.last());
                    context.pop();
                    context.pop();
                    break;
                case Instruction.GREATER:
                    calc = new Variable(new Boolean(((Number) context.last(1).getValue()).getValue() > ((Number) context.last().getValue()).getValue()));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.GREATER_EQUALS:
                    calc = new Variable(new Boolean(((Number) context.last(1).getValue()).getValue() >= ((Number) context.last().getValue()).getValue()));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.LESSER:
                    calc = new Variable(new Boolean(((Number) context.last(1).getValue()).getValue() < ((Number) context.last().getValue()).getValue()));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.LESSER_EQUALS:
                    calc = new Variable(new Boolean(((Number) context.last(1).getValue()).getValue() <= ((Number) context.last().getValue()).getValue()));
                    context.pop();
                    context.pop();
                    context.push(calc);
                    break;
                case Instruction.ARRAY_LENGTH:
                    context.setLast(new Variable(new Number(((Table) context.last().getValue()).length())));
                    break;
                case Instruction.TABLE_SIZE:
                    context.setLast(new Variable(new Number(((Table) context.last().getValue()).size())));
                    break;
            }
        }
        //System.out.println(Arrays.toString(context.getStack().toArray()));
        return null;
    }

    @Override
    public Object value() {
        return this;
    }
}
