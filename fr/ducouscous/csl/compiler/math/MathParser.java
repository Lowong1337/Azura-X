package fr.ducouscous.csl.compiler.math;

import fr.ducouscous.csl.compiler.CompileFunction;
import fr.ducouscous.csl.compiler.LexerComponent;
import fr.ducouscous.csl.compiler.LexerType;
import fr.ducouscous.csl.running.Instruction;

import java.util.*;

public class MathParser {
    public static final HashMap<String, Operator> OPERATORS = new HashMap<>();
    static {
        OPERATORS.put("+", Operator.ADD);
        OPERATORS.put("-", Operator.SUBTRACT);
        OPERATORS.put("*", Operator.MULTIPLY);
        OPERATORS.put("/", Operator.DIVIDE);
        OPERATORS.put(">>", Operator.BITWISE_RIGHT);
        OPERATORS.put("<<", Operator.BITWISE_LEFT);
        OPERATORS.put("&", Operator.AND);
        OPERATORS.put("|", Operator.OR);
        OPERATORS.put("^", Operator.XOR);
        OPERATORS.put("&&", Operator.BOOLEAN_AND);
        OPERATORS.put("||", Operator.BOOLEAN_OR);
        OPERATORS.put("&|", Operator.BOOLEAN_XOR);
        OPERATORS.put("<", Operator.LESSER);
        OPERATORS.put(">", Operator.GREATER);
        OPERATORS.put(">=", Operator.GREATER_EQUALS);
        OPERATORS.put("<=", Operator.LESSER_EQUALS);
        OPERATORS.put("==", Operator.EQUALS);
        OPERATORS.put("%", Operator.MODULO);
        OPERATORS.put("..", Operator.CONCAT);
        OPERATORS.put("############", Operator.ADD);
    }

    private final CompileFunction function;
    public MathParser(CompileFunction function) {
        this.function = function;
    }

    public void parseComponents(List<LexerComponent> components, List<MathComponent> c) {
        List<LexerComponent> current = new ArrayList<>();

        function.update(components, update -> {
            if (update.isOut() && update.getCurrent().is(LexerType.OPERATOR)) {
                if (function.isMath(current)) {
                    parseComponents(current, c);
                } else {
                    c.add(new MathCode(current.toArray(current.toArray(new LexerComponent[0]))));
                }
                c.add(new MathOperator(OPERATORS.get(update.getCurrent().getData().toString())));
                current.clear();
            } else {
                current.add(update.getCurrent());
            }
        });

        if (function.isMath(current)) {
            parseComponents(current, c);
        } else {
            c.add(new MathCode(current.toArray(current.toArray(new LexerComponent[0]))));
        }
        patchPriority(c);
    }

    public void patchPriority(List<MathComponent> components) {
        List<MathComponent> out = new ArrayList<>(), current = new ArrayList<>();
        boolean p = false, a = false;
        int skip = 0;
        int i = 0;
        for (MathComponent component : components) {
            if (skip > 0) {
                skip--;
                continue;
            }
            if (component instanceof MathOperator) {
                boolean flag = ((MathOperator) component).getOperator().priority;
                if (flag) {
                    if (a)
                        current.remove(current.size() - 1);
                    current.add(components.get(i-1));
                    current.add(component);
                    current.add(components.get(i+1));
                    i+=2;
                    skip = 1;
                    p = true;
                    a = true;
                    continue;
                } else if (p) {
                    out.remove(out.size() - 1);
                    out.add(new MathList(current.toArray(new MathComponent[0])));
                    p = false;
                }
                a = false;
            }
            out.add(component);
            i++;
        }
        if (p) {
            out.remove(out.size() - 1);
            out.add(new MathList(current.toArray(new MathComponent[0])));
        }
        components.clear();
        components.addAll(out);
    }

    public void calc(List<MathComponent> components, List<Instruction> buffer) {
        if (components.size() == 1) {
            calc(Arrays.asList(((MathList) components.get(0)).getComponents()), buffer);
            return;
        }
        int i = 3;
        if (components.get(2) instanceof MathCode) {
            MathCode code = (MathCode) components.get(2);

            function.parseComponent(Arrays.asList(code.getComponents()), buffer, 1);
        } else {
            MathList list = (MathList) components.get(2);

            calc(Arrays.asList(list.getComponents()), buffer);
        }
        if (components.get(0) instanceof MathCode) {
            MathCode code = (MathCode) components.get(0);

            function.parseComponent(Arrays.asList(code.getComponents()), buffer, 1);
        } else {
            MathList list = (MathList) components.get(0);

            calc(Arrays.asList(list.getComponents()), buffer);
        }
        Collections.addAll(buffer, ((MathOperator) components.get(1)).getOperator().instructions);

        while (i < components.size()) {
            int nIndex = i + 1;
            int opIndex = i;
            if (components.get(nIndex) instanceof MathCode) {
                MathCode code = (MathCode) components.get(nIndex);

                List<Instruction> b = new ArrayList<>();
                function.parseComponent(Arrays.asList(code.getComponents()), b, 1);
                buffer.addAll(0, b);
            } else {
                MathList list = (MathList) components.get(nIndex);

                calc(Arrays.asList(list.getComponents()), buffer);
            }

            buffer.addAll(Arrays.asList(((MathOperator) components.get(opIndex)).getOperator().instructions));
            i += 2;
        }
    }

    public void parse(List<LexerComponent> components, List<Instruction> buffer) {
        List<MathComponent> b = new ArrayList<>();
        parseComponents(components, b);
        calc(b, buffer);
    }
}
