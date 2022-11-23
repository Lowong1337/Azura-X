package fr.ducouscous.csl.compiler;

import fr.ducouscous.csl.compiler.components.*;
import fr.ducouscous.csl.compiler.math.MathParser;
import fr.ducouscous.csl.compiler.math.Operator;
import fr.ducouscous.csl.running.Instruction;
import fr.ducouscous.csl.util.Pointer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompileFunction {
    private final List<LexerComponent> components;
    private final List<Instruction> buffer;
    private final List<String> variables = new ArrayList<>(), arguments = new ArrayList<>();
    private final boolean main;

    public List<String> getArguments() {
        return arguments;
    }

    public CompileFunction(boolean main, List<LexerComponent> components, List<Instruction> buffer) {
        this.main = main;
        this.components = components;
        this.buffer = buffer;
    }

    public void update(List<LexerComponent> components, LexerUpdate update) {
        CurrentUpdate current = new CurrentUpdate();
        int i = 0;
        for (LexerComponent component : components) {
            current.setCurrent(component);
            if (i < components.size() - 1)
                current.setNext(components.get(i + 1));
            if (i > 0)
                current.setPrevious(components.get(i - 1));
            current.setLast(components.size() - 1 == i);
            if (component.is(LexerType.R_PAREN)) current.setParen(current.getParen() - 1);
            if (component.is(LexerType.L_PAREN)) current.setParen(current.getParen() + 1);
            if (component.is(LexerType.R_HOOK)) current.setHook(current.getHook() - 1);
            if (component.is(LexerType.L_HOOK)) current.setHook(current.getHook() + 1);
            if (component.is(LexerType.R_BRACKET)) current.setBracket(current.getBracket() - 1);
            if (component.is(LexerType.L_BRACKET)) current.setBracket(current.getBracket() + 1);
            update.update(current);
            i++;
        }
    }

    boolean endsWith(List<LexerComponent> components, LexerType type) {
        return components.get(components.size()-1).is(type);
    }

    boolean isBooleanMath(List<LexerComponent> components) {
        boolean[] gud = new boolean[1];
        update(components, update -> {
            if (update.isOut() && update.getCurrent().is(LexerType.OPERATOR)) {
                if (MathParser.OPERATORS.get(update.getCurrent().getData().toString()).boolOperator) {
                    gud[0] = true;
                }
            }
        });

        return gud[0];
    }

    Operator getBooleanMath(List<LexerComponent> components) {
        Operator[] gud = new Operator[1];
        update(components, update -> {
            if (update.isOut() && update.getCurrent().is(LexerType.OPERATOR)) {
                if (MathParser.OPERATORS.get(update.getCurrent().getData().toString()).boolOperator) {
                    gud[0] = MathParser.OPERATORS.get(update.getCurrent().getData().toString());
                }
            }
        });

        return gud[0];
    }

    String getOperator(List<LexerComponent> components) {
        String[] gud = new String[1];
        update(components, update -> {
            if (update.isOut() && update.getCurrent().is(LexerType.OPERATOR)) {
                gud[0] = update.getCurrent().getData().toString();
            }
        });

        return gud[0];
    }

    boolean containsEquals(List<LexerComponent> components) {
        boolean[] gud = new boolean[1];
        update(components, (update) -> {
            if (update.getCurrent().is(LexerType.EQUALS) && update.isOut()) {
                gud[0] = true;
            }
        });
        return gud[0];
    }

    public List<LexerComponent>[] split(List<LexerComponent> components, LexerType type) {
        List<List<LexerComponent>> b = new ArrayList<>();
        Pointer<List<LexerComponent>> current = new Pointer<>(new ArrayList<>());

        update(components, (update) -> {
            if (update.isOut() && update.getCurrent().is(type)) {
                b.add(current.instance);
                current.instance = new ArrayList<>();
            } else {
                current.instance.add(update.getCurrent());
            }
        });
        if (!current.instance.isEmpty()) {
            b.add(current.instance);
        }

        return b.toArray(new List[0]);
    }

    List<LexerComponent>[] splitMath(List<LexerComponent> components, String operator) {
        List<List<LexerComponent>> out = new ArrayList<>();
        List<LexerComponent>[] current = new List[] {new ArrayList<>()};

        update(components, (update) -> {
            if (update.isOut() && update.getCurrent().getData() != null && update.getCurrent().getData().equals(operator)) {
                out.add(current[0]);
                current[0] = new ArrayList<>();
            } else {
                current[0].add(update.getCurrent());
            }
        });
        if (!current[0].isEmpty()) {
            out.add(current[0]);
        }

        return out.toArray((ArrayList<LexerComponent> []) new ArrayList[0]);
    }

    public boolean isMath(List<LexerComponent> components) {
        boolean[] gud = new boolean[1];

        update(components, update -> {
            if (update.isOut()) {
                if (update.getCurrent().is(LexerType.OPERATOR)) {
                    if (!MathParser.OPERATORS.get(update.getCurrent().getData().toString()).boolOperator) {
                        gud[0] = true;
                    }
                }
            }
        });

        return gud[0];
    }

    public boolean isInParen(List<LexerComponent> components) {
        int[] state = { 0 };
        boolean[] end = { false, false };
        update(components, update -> {
            if (end[0]) return;
            if (state[0] == 0) {
                state[0] = 1;
                if (!update.getCurrent().is(LexerType.L_PAREN)) {
                    end[0] = true;
                }
            } else if (update.isLast()) {
                if (update.getCurrent().is(LexerType.R_PAREN)) {
                    end[1] = end[0] = true;
                }
            } else if (update.isOut()) {
                end[0] = true;
                end[1] = false;
            }
        });
        return end[1];
    }

    public boolean isArrayAccess(List<LexerComponent> components) {
        int state = 0;
        int a = 0;
        for (int i = components.size() - 1; i >= 0; i--) {
            if (components.get(i).is(LexerType.L_HOOK)) a++;
            if (!components.get(i).is(LexerType.R_HOOK) && i == components.size() - 1) {
                return false;
            } else if (state == 0) {
                state = 1;
            }
            if (state == 2) {
                return true;
            }
            if (components.get(i).is(LexerType.L_HOOK)) {
                if (a == 0)
                    state = 2;
            }
            if (components.get(i).is(LexerType.R_HOOK)) a--;
        }
        return false;
    }

    List<Operator> getBooleanOperators(List<LexerComponent> components) {
        List<Operator> out = new ArrayList<>();
        update(components, (update) -> {
            if (update.isOut() && update.getCurrent().is(LexerType.OPERATOR) && MathParser.OPERATORS.get(update.getCurrent().getData().toString()).boolOperator) {
                out.add(MathParser.OPERATORS.get(update.getCurrent().getData().toString()));
            }
        });

        return out;
    }

    List<LexerComponent>[] splitBooleanMath(List<LexerComponent> components) {
        List<List<LexerComponent>> out = new ArrayList<>();
        List<LexerComponent> current = new ArrayList<>();
        update(components, (update) -> {
            if (update.isOut() && update.getCurrent().is(LexerType.OPERATOR) && MathParser.OPERATORS.get(update.getCurrent().getData().toString()).boolOperator) {
                out.add(new ArrayList<>(current));
                current.clear();
            } else {
                current.add(update.getCurrent());
            }
        });
        out.add(new ArrayList<>(current));
        return out.toArray((List<LexerComponent>[]) new List[0]);
    }

    public void parseComponent(List<LexerComponent> components, List<Instruction> buffer, int state) {
        List<Instruction> b = new ArrayList<>();
        if (isBooleanMath(components)) {
            List<LexerComponent>[] split = splitBooleanMath(components);
            List<Operator> operators = getBooleanOperators(components);
            parseComponent(split[0], b, state + 1);
            int i = 1;
            for (Operator operator : operators) {
                parseComponent(split[i], b, state + 1);
                b.addAll(Arrays.asList(operator.instructions));
                i++;
            }
        } else if (isMath(components)) {
            MathParser parser = new MathParser(this);
            parser.parse(components, b);
        } else if (isArrayAccess(components)) {
            int a = 0, s = 0;
            List<LexerComponent> c = new ArrayList<>(), o = new ArrayList<>();
            for (int i = components.size() - 1; i >= 0; i--) {
                if (s == 0) {
                    if (components.get(i).is(LexerType.R_HOOK)) a--;
                    if (components.get(i).is(LexerType.L_HOOK)) a++;
                    c.add(0, components.get(i));
                    if (a == 0) s = 1;
                } else {
                    o.add(0, components.get(i));
                }
            }
            parseComponent(o, buffer, state + 1);
            parseComponent(c.subList(1, c.size()-1), buffer, state + 1);
            b.add(new Instruction(Instruction.TABLE_GET));
        } else if (components.get(0).is(LexerType.INVERT_BOOL)) {
            parseComponent(components.subList(1, components.size()), b, state + 1);
            b.add(new Instruction(Instruction.NOT));
        } else if (components.get(0).is(LexerType.BOOLEAN)) {
            b.add(new Instruction(Instruction.LDC, Instruction.LDC_BOOL, components.get(0).getData()));
        } else if (components.get(0).is(LexerType.NUMBER)) {
            b.add(new Instruction(Instruction.LDC, Instruction.LDC_NUMBER, components.get(0).getData()));
        } else if (components.get(0).is(LexerType.STRING)) {
            b.add(new Instruction(Instruction.LDC, Instruction.LDC_STRING, components.get(0).getData()));
        } else if (components.get(0).is(LexerType.L_HOOK) && endsWith(components, LexerType.R_HOOK)) {
            List<LexerComponent>[] split = split(components.subList(1, components.size() - 1), LexerType.COMMA);
            b.add(new Instruction(Instruction.LDC, Instruction.LDC_TABLE));
            for (List<LexerComponent> lexerComponents : split) {
                if (containsEquals(lexerComponents)) {
                    List<LexerComponent>[] s = split(lexerComponents, LexerType.EQUALS);
                    parseComponent(s[1], b, state + 1);
                    b.add(new Instruction(Instruction.TABLE_SET, s[0].get(0).getData().toString()));
                } else {
                    parseComponent(lexerComponents, b, state + 1);
                    b.add(new Instruction(Instruction.ARRAY_PUSH));
                }
            }
        } else if (isInParen(components)) {
            parseComponent(components.subList(1, components.size() - 1), b, state + 1);
        } else if (components.get(0).is(LexerType.NAME)) {
            List<LexerComponent>[] split = split(components, LexerType.DOT);
            for (int i = split.length - 1; i >= 0; i--) {
                List<LexerComponent> lexerComponents = split[i];
                if (endsWith(lexerComponents, LexerType.R_PAREN)) {
                    List<LexerComponent> c = lexerComponents.subList(2, lexerComponents.size() - 1);
                    List<LexerComponent>[] s = split(c, LexerType.COMMA);
                    for (List<LexerComponent> lexerComponentList : s) {
                        if (!lexerComponentList.isEmpty())
                            parseComponent(lexerComponentList, b, state + 1);
                    }
                }
            }
            int i = 0;
            for (List<LexerComponent> c : split) {
                if (i == 0) {
                    if (c.size() == 1) {
                        if (c.get(0).getData().equals("global")) {
                            b.add(new Instruction(Instruction.GLOBAL));
                        } else if (variables.contains(c.get(0).getData().toString())) {
                            b.add(new Instruction(Instruction.LOAD, variables.indexOf(c.get(0).getData().toString())));
                        } else if (arguments.contains(c.get(0).getData().toString())) {
                            b.add(new Instruction(Instruction.ARG, arguments.indexOf(c.get(0).getData().toString())));
                        } else {
                            b.add(new Instruction(Instruction.GLOBAL));
                            b.add(new Instruction(Instruction.GET, c.get(0).getData()));
                        }
                    } else if (c.size() == 3) {
                        if (c.get(0).getData().equals("global")) {
                            b.add(new Instruction(Instruction.GLOBAL));
                        } else if (variables.contains(c.get(0).getData().toString())) {
                            b.add(new Instruction(Instruction.LOAD, variables.indexOf(c.get(0).getData().toString())));
                        } else if (arguments.contains(c.get(0).getData().toString())) {
                            b.add(new Instruction(Instruction.ARG, arguments.indexOf(c.get(0).getData().toString())));
                        } else {
                            b.add(new Instruction(Instruction.GLOBAL));
                            b.add(new Instruction(Instruction.GET, c.get(0).getData()));
                        }
                        b.add(new Instruction(Instruction.CALL, 0));
                    } else {
                        if (c.get(0).getData().equals("global")) {
                            b.add(new Instruction(Instruction.GLOBAL));
                        } else if (variables.contains(c.get(0).getData().toString())) {
                            b.add(new Instruction(Instruction.LOAD, variables.indexOf(c.get(0).getData().toString())));
                        } else if (arguments.contains(c.get(0).getData().toString())) {
                            b.add(new Instruction(Instruction.ARG, arguments.indexOf(c.get(0).getData().toString())));
                        } else {
                            b.add(new Instruction(Instruction.GLOBAL));
                            b.add(new Instruction(Instruction.GET, c.get(0).getData()));
                        }
                        b.add(new Instruction(Instruction.CALL, split(c.subList(2, c.size()-1), LexerType.COMMA).length));
                    }
                } else {
                    b.add(new Instruction(Instruction.GET, c.get(0).getData()));
                    if (endsWith(c, LexerType.R_PAREN))
                        b.add(new Instruction(Instruction.CALL, split(c.subList(2, c.size()-1), LexerType.COMMA).length));
                }
                i++;
                if (state == 0 && i == split.length && c.size() > 1) {
                    b.add(new Instruction(Instruction.POP));
                }
            }

            //System.out.println(Arrays.toString(b.toArray()));
        } else if (components.get(0).is(LexerType.KEYWORD)) {
            switch (((Keyword) components.get(0).getData())) {
                case RETURN:
                    if (components.size() > 0)
                        parseComponent(components.subList(1, components.size()), buffer, 1);
                    else
                        buffer.add(new Instruction(Instruction.NULL));
                    buffer.add(new Instruction(Instruction.RETURN));
                    break;
                case BREAK:
                    buffer.add(new Instruction(Instruction.GOTO, loops.get(loops.size() - 1)+"-1"));
                    break;
                case CONTINUE:
                    buffer.add(new Instruction(Instruction.GOTO, loops.get(loops.size() - 1)+"-0"));
                    break;
            }
        }
        buffer.addAll(b);
    }

    void registerVariable(String var) {
        variables.add(var);
        variableCount.set(variableCount.size() - 1, variableCount.get(variableCount.size() - 1) + 1);
    }

    void startStatement() {
        variableCount.add(0);
    }

    void endStatement() {
        for (int i = 0; i < variableCount.get(variableCount.size() - 1); i++) {
            variables.remove(variables.size() - 1);
        }
        variableCount.remove(variableCount.size() - 1);
    }

    List<String> loops = new ArrayList<>();
    public void parseLine(List<LexerComponent> components) {
        if (components.isEmpty()) return;
        if (components.get(0).is(LexerType.OPERATOR) && components.get(1).is(LexerType.OPERATOR)) {
            Operator operator = MathParser.OPERATORS.get(components.get(0).getData().toString());
            List<LexerComponent> c = components.subList(2, components.size());
            parseComponent(c, buffer, 0);
            switch (operator) {
                case ADD:
                    buffer.add(new Instruction(Instruction.INCREMENT));
                    break;
                case SUBTRACT:
                    buffer.add(new Instruction(Instruction.DECREMENT));
                    break;
            }
            buffer.add(new Instruction(Instruction.POP));
        } else if (components.get(components.size() - 1).is(LexerType.OPERATOR) && components.get(components.size() - 2).is(LexerType.OPERATOR)) {
            Operator operator = MathParser.OPERATORS.get(components.get(components.size() - 1).getData().toString());
            List<LexerComponent> c = components.subList(0, components.size() - 2);
            parseComponent(c, buffer, 0);
            switch (operator) {
                case ADD:
                    buffer.add(new Instruction(Instruction.INCREMENT));
                    break;
                case SUBTRACT:
                    buffer.add(new Instruction(Instruction.DECREMENT));
                    break;
            }
            buffer.add(new Instruction(Instruction.POP));
        } else if (containsEquals(components)) {
            List<LexerComponent>[] split = split(components, LexerType.EQUALS);
            if (split[0].get(0).is(LexerType.KEYWORD)) {
                if (main) {
                    buffer.add(new Instruction(Instruction.GLOBAL));
                }
                parseComponent(split[1], buffer, 1);
                if (main) {
                    buffer.add(new Instruction(Instruction.TABLE_SET, split[0].get(1).getData().toString()));
                    buffer.add(new Instruction(Instruction.POP));
                } else {
                    buffer.add(new Instruction(Instruction.STORE, variables.size()));
                    registerVariable(split[0].get(1).getData().toString());
                }
            } else {
                if (endsWith(split[0], LexerType.OPERATOR)) {
                    parseComponent(split[1], buffer, 1);
                    parseComponent(split[0].subList(0, split[0].size()-1), buffer, 1);
                    buffer.addAll(Arrays.asList(MathParser.OPERATORS.get(split[0].get(split[0].size() - 1).getData().toString()).instructions));
                    parseComponent(split[0].subList(0, split[0].size()-1), buffer, 1);
                    buffer.add(new Instruction(Instruction.SET));
                } else {
                    parseComponent(split[1], buffer, 1);
                    parseComponent(split[0], buffer, 1);
                    buffer.add(new Instruction(Instruction.SET));
                }
            }
        } else {
            parseComponent(components, buffer, 0);
        }
    }

    public void postBlockStatement(Keyword keyword) {
        switch (keyword) {
            case ELSE:
            case IF:
                buffer.add(new Instruction(Instruction.LABEL, label.get(label.size() - 1)+"-end"));
                break;
            case FOR:
            case WHILE:
                buffer.add(new Instruction(Instruction.GOTO, label.get(label.size() - 1)+"-start"));
                buffer.add(new Instruction(Instruction.LABEL, label.get(label.size() - 1)+"-end"));
                break;
        }
        endStatement();
        label.remove(label.size() - 1);
    }

    List<Keyword> statements = new ArrayList<>();
    List<String> label = new ArrayList<>();
    int i = 0, componentIndex = 0;

    int readBlock(int index, List<LexerComponent> components, List<LexerComponent> condition, List<CompileComponent> componentList, List<CompileComponent> buffer, Keyword kw) {
        if (components.get(index).is(LexerType.L_BRACKET)) {
            List<LexerComponent> lexerComponents = new ArrayList<>();
            int bracket = 0;
            boolean start = true;
            while (start || bracket > 0) {
                if (components.get(index).is(LexerType.R_BRACKET)) bracket--;
                if (components.get(index).is(LexerType.L_BRACKET)) bracket++;

                lexerComponents.add(components.get(index));

                start = false;
                index++;
            }
            lexerComponents.remove(0);
            lexerComponents.remove(lexerComponents.size() - 1);
            for (List<LexerComponent> lexerComponentList : split(lexerComponents, LexerType.NEW_LINE)) {
                lexerComponentList.add(new LexerComponent(LexerType.NEW_LINE));
                readComponent(0, componentList, lexerComponentList);
            }
        } else {
            index = readComponent(index, componentList, components);
        }
        if (kw.hasParen) {
            buffer.add(new CompileParenBlock(kw, componentList.toArray(new CompileComponent[0]), condition.toArray(new LexerComponent[0])));
        } else {
            buffer.add(new CompileBlock(kw, componentList.toArray(new CompileComponent[0])));
        }

        return index;
    }

    List<CompileComponent> compileComponents = new ArrayList<>();

    int readComponent(int index, List<CompileComponent> buffer, List<LexerComponent> components) {
        if (components.get(index).is(LexerType.KEYWORD) && ((Keyword) components.get(index).getData()).block) {
            Keyword kw = ((Keyword) components.get(index).getData());
            List<CompileComponent> componentList = new ArrayList<>();
            List<LexerComponent> condition = new ArrayList<>();
            if (kw.hasParen) {
                int paren = 0;
                boolean start = true;
                while (start || paren > 0) {
                    if (components.get(++index).is(LexerType.R_PAREN)) paren--;
                    if (components.get(index).is(LexerType.L_PAREN)) paren++;

                    condition.add(components.get(index));

                    start = false;
                }
            }
            index++;
            if (kw == Keyword.IF) {
                List<CompileComponent> ifBlock = new ArrayList<>(), elseBlock = new ArrayList<>();
                index = readBlock(index, components, condition, componentList, ifBlock, kw);
                componentList.clear();
                if (index < components.size() && components.get(index).getData() == Keyword.ELSE) {
                    index++;
                    index = readBlock(index, components, new ArrayList<>(), componentList, elseBlock, Keyword.ELSE);
                    buffer.add(new CompileIfBlock((CompileParenBlock) ifBlock.get(0), (CompileBlock) elseBlock.get(0)));
                } else {
                    buffer.add(new CompileIfBlock((CompileParenBlock) ifBlock.get(0), null));
                }
            } else {
                index = readBlock(index, components, condition, componentList, buffer, kw);
                index--;
            }
        } else {
            List<LexerComponent> lexerComponents = new ArrayList<>();
            while (!components.get(index).is(LexerType.NEW_LINE)) {
                lexerComponents.add(components.get(index++));
            }
            buffer.add(new CompileStatement(lexerComponents.toArray(new LexerComponent[0])));
        }

        return index;
    }

    public void parseIf(CompileIfBlock compileIfBlock) {
        if (compileIfBlock.getElseBlock() == null) {
            preBlockStatement(Keyword.IF);
            parseComponent(Arrays.asList(compileIfBlock.getIfBlock().getParen()), buffer, 1);
            buffer.add(new Instruction(Instruction.NOT));
            buffer.add(new Instruction(Instruction.IF, i+"-end"));

            for (CompileComponent component : compileIfBlock.getIfBlock().getComponents()) {
                parse(component);
            }
            postBlockStatement(Keyword.IF);
        } else {
            preBlockStatement(Keyword.IF);
            parseComponent(Arrays.asList(compileIfBlock.getIfBlock().getParen()), buffer, 1);
            buffer.add(new Instruction(Instruction.NOT));
            buffer.add(new Instruction(Instruction.IF, i+"-end"));

            for (CompileComponent component : compileIfBlock.getIfBlock().getComponents()) {
                parse(component);
            }
            postBlockStatement(Keyword.IF);
            preBlockStatement(Keyword.ELSE);

            for (CompileComponent component : compileIfBlock.getElseBlock().getComponents()) {
                parse(component);
            }

            postBlockStatement(Keyword.ELSE);
        }
    }

    void parseParenBlock(CompileParenBlock block) {
        if (block.getKeyword() == Keyword.WHILE) {
            preBlockStatement(Keyword.WHILE);
            parseComponent(Arrays.asList(block.getParen()), buffer, 1);
            buffer.add(new Instruction(Instruction.NOT));
            buffer.add(new Instruction(Instruction.IF, i+"-end"));

            for (CompileComponent component : block.getComponents()) {
                parse(component);
            }

            postBlockStatement(Keyword.WHILE);
        }
        if (block.getKeyword() == Keyword.FOR) {
            startStatement();
            List<LexerComponent>[] split = split(Arrays.asList(block.getParen()).subList(1, block.getParen().length - 1), LexerType.COLON);
            List<LexerComponent>[] splitKey = split(split[0], LexerType.COMMA);
            String forVar = splitKey[0].get(0).getData().toString();
            if (splitKey.length == 1) {
                registerVariable(forVar+"-index");
                registerVariable(forVar);
                buffer.add(new Instruction(Instruction.LDC, Instruction.LDC_NUMBER, 0.0));
                buffer.add(new Instruction(Instruction.STORE, variables.indexOf(forVar+"-index")));
            }
            preBlockStatement(Keyword.FOR);
            if (splitKey.length == 1) {
                parseComponent(split[1], buffer, 0);
                buffer.add(new Instruction(Instruction.DUP));
                buffer.add(new Instruction(Instruction.ARRAY_LENGTH));
                buffer.add(new Instruction(Instruction.LOAD, variables.indexOf(forVar+"-index")));
                buffer.add(new Instruction(Instruction.LESSER_EQUALS));
                buffer.add(new Instruction(Instruction.IF, (i + "-end")));
                buffer.add(new Instruction(Instruction.LOAD, variables.indexOf(forVar+"-index")));
                buffer.add(new Instruction(Instruction.ARRAY_GET));
                buffer.add(new Instruction(Instruction.STORE, variables.indexOf(forVar)));
            }

            for (CompileComponent component : block.getComponents()) {
                parse(component);
            }

            buffer.add(new Instruction(Instruction.LOAD, variables.indexOf(forVar+"-index")));
            buffer.add(new Instruction(Instruction.INCREMENT));
            postBlockStatement(Keyword.FOR);
        }
    }

    void parse(CompileComponent compileComponent) {
        if (compileComponent instanceof CompileStatement) {
            parseLine(Arrays.asList(((CompileStatement) compileComponent).getComponents()));
        }
        if (compileComponent instanceof CompileIfBlock) {
            parseIf((CompileIfBlock) compileComponent);
        }
        if (compileComponent instanceof CompileParenBlock) {
            parseParenBlock((CompileParenBlock) compileComponent);
        }
    }
    public void compile() {
        compileComponents.clear();
        for (int index = 0; index < getComponents().size(); index++) {
            index = readComponent(index, compileComponents, getComponents());
        }
        for (CompileComponent compileComponent : compileComponents) {
            parse(compileComponent);
        }
    }

    List<Integer> variableCount = new ArrayList<>();
    private void preBlockStatement(Keyword keyword) {
        if (keyword != Keyword.FOR) {
            startStatement();
        }
        statements.add(keyword);
        label.add(++i + "");
        switch (keyword) {
            case ELSE:
                buffer.add(buffer.size() - 1, new Instruction(Instruction.GOTO, i+"-end"));
                break;
            case WHILE:
            case FOR:
                buffer.add(new Instruction(Instruction.LABEL, i+"-start"));
                break;
        }
    }

    public List<LexerComponent> getComponents() {
        return components;
    }
}
