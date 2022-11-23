package fr.ducouscous.csl.compiler;

import fr.ducouscous.csl.compiler.math.MathParser;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {
    private final List<LexerComponent> components = new ArrayList<>();
    boolean endsWithKeyword(String str) {
        for (Keyword keyword : Keyword.values()) {
            if (str.endsWith(keyword.name)) return true;
        }
        return false;
    }
    boolean endsWithKeyword(String str, int index) {
        for (Keyword keyword : Keyword.values()) {
            if (str.startsWith(keyword.name, index)) return true;
        }
        return false;
    }
    Keyword getKeyword(String str) {
        Keyword out = null;
        for (Keyword keyword : Keyword.values()) {
            if (str.endsWith(keyword.name) && (out == null || out.name.length() < keyword.name.length())) {
                out = keyword;
            }
        }
        return out;
    }

    public String patch(String str) {
        StringBuilder buffer = new StringBuilder();
        boolean startOfLine = false, inString = false;
        int index = 0;
        char pre = 0;
        for (char c : str.toCharArray()) {
            index++;
            if (startOfLine) {
                if (c == ' ' || c == '\t') continue;
                else startOfLine = false;
            }
            if (c == '"' && !(pre == '\\' && inString)) inString = !inString;
            if ((c == ' ') && !inString && !endsWithKeyword(buffer.toString()) && !endsWithKeyword(str, index))
                continue;
            if (c == '\n' || c == ';') startOfLine = true;
            if (c != '\n')
                buffer.append(c);
            pre = c;
        }
        return buffer.toString();
    }

    Keyword getStartKeyword(String str) {
        for (Keyword value : Keyword.values()) {
            if (str.startsWith(value.name)) {
                return value;
            }
        }
        return null;
    }

    public LexerComponent[] readComponents(String str) {
        List<LexerComponent> components = new ArrayList<>();

        if (str.matches("\\d+(\\.\\d+)?")) {
            components.add(new LexerComponent(LexerType.NUMBER, Double.parseDouble(str)));
        } else if (str.matches("\\w+")) {
            if (getKeyword(str) != null) {
                components.add(new LexerComponent(LexerType.KEYWORD, getKeyword(str)));
            } else if (str.matches("true|false")) {
                components.add(new LexerComponent(LexerType.BOOLEAN, str.equals("true")));
            } else if (getStartKeyword(str) != null) {
                while (getStartKeyword(str) != null) {
                    Keyword k = getStartKeyword(str);
                    str = str.substring(k.name.length());
                    components.add(new LexerComponent(LexerType.KEYWORD, k));
                }
                if (!str.isEmpty()) {
                    components.add(new LexerComponent(LexerType.NAME, str));
                }
            } else {
                components.add(new LexerComponent(LexerType.NAME, str));
            }
        } else {
            if (isOperator(str)) {
                components.add(new LexerComponent(LexerType.OPERATOR, str));
            } else {
                StringBuilder op = new StringBuilder();
                for (char c1 : str.toCharArray()) {
                    boolean flag = false;
                    switch (c1) {
                        case '{':
                            components.add(new LexerComponent(LexerType.L_BRACKET));
                            break;
                        case '}':
                            components.add(new LexerComponent(LexerType.R_BRACKET));
                            break;
                        case '(':
                            components.add(new LexerComponent(LexerType.L_PAREN));
                            break;
                        case ')':
                            components.add(new LexerComponent(LexerType.R_PAREN));
                            break;
                        case '[':
                            components.add(new LexerComponent(LexerType.L_HOOK));
                            break;
                        case ']':
                            components.add(new LexerComponent(LexerType.R_HOOK));
                            break;
                        case ',':
                            components.add(new LexerComponent(LexerType.COMMA));
                            break;
                        case '!':
                            components.add(new LexerComponent(LexerType.INVERT_BOOL));
                            break;
                        case '.':
                            components.add(new LexerComponent(LexerType.DOT));
                            break;
                        case '=':
                            components.add(new LexerComponent(LexerType.EQUALS));
                            break;
                        case ':':
                            components.add(new LexerComponent(LexerType.COLON));
                            break;
                        case ';':
                            components.add(new LexerComponent(LexerType.NEW_LINE));
                            break;
                        default:
                            op.append(c1);
                            flag = true;
                            break;
                    }
                    if (!flag && (op.length() > 0)) {
                        components.add(components.size() - 1, new LexerComponent(LexerType.OPERATOR, op.toString()));
                        op = new StringBuilder();
                    }
                }

                if (op.length() > 0) {
                    while (startsWithOperator(op.toString())) {
                        String operator = getStartOperator(op.toString());
                        components.add(new LexerComponent(LexerType.OPERATOR, operator));
                        assert operator != null;
                        op = new StringBuilder(op.substring(operator.length()));
                    }
                }
            }
        }

        return components.toArray(new LexerComponent[0]);
    }

    public void read(String str) {
        String line = patch(str);
        components.clear();
        boolean inString = false;
        StringBuilder current = new StringBuilder();
        boolean old = false;
        int skip = 0;
        for (int i = 0; i < line.toCharArray().length; i++) {
            char c = line.charAt(i);
            if (skip > 0) {
                skip--;
                continue;
            }
            if (inString) {
                if (c == '"') {
                    inString = false;
                    components.add(new LexerComponent(LexerType.STRING, current.toString()));
                    current = new StringBuilder();
                } else {
                    if (c == '\\') {
                        skip = 1;
                        switch (line.charAt(i + 1)) {
                            case 'n':
                                current.append('\n');
                                break;
                            case 'b':
                                current.append('\b');
                                break;
                            case 'f':
                                current.append('\f');
                                break;
                            case 't':
                                current.append('\t');
                                break;
                            case 'r':
                                current.append('\r');
                                break;
                            case '\\':
                                current.append('\\');
                                break;
                            case '"':
                                current.append('"');
                                break;
                        }
                        continue;
                    }
                    current.append(c);
                }
            } else {
                if (c == '"') {
                    components.addAll(Arrays.asList(readComponents(current.toString())));
                    current = new StringBuilder();
                    inString = true;
                } else {
                    if (c == '.') {
                        if (!current.toString().matches("\\d+")) {
                            components.add(new LexerComponent(LexerType.NAME, current.toString()));
                            components.add(new LexerComponent(LexerType.DOT));
                            current = new StringBuilder();
                        } else {
                            current.append(c);
                        }
                    } else {
                        boolean n = (c + "").matches("\\w+");
                        if (n != old || c == ';') {
                            if (!current.toString().isEmpty()) {
                                components.addAll(Arrays.asList(readComponents(current.toString())));
                                current = new StringBuilder();
                            }
                        }
                        if (c != ' ')
                            current.append(c);
                        old = n;
                    }
                }
            }
        }
        if (current.length() > 0)
            components.addAll(Arrays.asList(readComponents(current.toString())));
    }

    private boolean isOperator(String operator) {
        return MathParser.OPERATORS.containsKey(operator);
    }

    private boolean startsWithOperator(String operator) {
        for (String s : MathParser.OPERATORS.keySet()) {
            if (operator.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    private String getStartOperator(String operator) {
        for (String s : MathParser.OPERATORS.keySet()) {
            if (operator.startsWith(s)) return s;
        }
        return null;
    }

    public List<LexerComponent> getComponents() {
        return components;
    }
}
