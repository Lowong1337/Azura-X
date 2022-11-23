package fr.ducouscous.csl.util;

public class StringContext {
    private final boolean inString;
    private final int paren, bracket;
    private final char current;

    public char getCurrent() {
        return current;
    }

    public int getParen() {
        return paren;
    }

    public boolean isOut() {
        return bracket == 0 && paren == 0;
    }

    public int getBracket() {
        return bracket;
    }

    public boolean isInString() {
        return inString;
    }

    public StringContext(char current, boolean inString, int p, int a) {
        this.paren = p;
        this.bracket = a;
        this.current = current;
        this.inString = inString;
    }
}
