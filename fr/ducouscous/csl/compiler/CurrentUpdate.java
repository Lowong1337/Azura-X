package fr.ducouscous.csl.compiler;

public class CurrentUpdate {
    private int bracket = 0, hook = 0, paren = 0;
    private LexerComponent current = null, next = null, previous = null;

    public void setPrevious(LexerComponent previous) {
        this.previous = previous;
    }

    public LexerComponent getPrevious() {
        return previous;
    }

    public void setBracket(int bracket) {
        this.bracket = bracket;
    }

    public void setCurrent(LexerComponent current) {
        this.current = current;
    }

    public void setHook(int hook) {
        this.hook = hook;
    }

    public void setParen(int paren) {
        this.paren = paren;
    }

    public LexerComponent getCurrent() {
        return current;
    }

    public LexerComponent getNext() {
        return next;
    }

    public void setNext(LexerComponent next) {
        this.next = next;
    }

    public int getBracket() {
        return bracket;
    }

    public int getHook() {
        return hook;
    }

    public int getParen() {
        return paren;
    }

    public boolean isOut() {
        return getBracket() == 0 && getHook() == 0 && getParen() == 0;
    }

    private boolean last = false;

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isLast() {
        return last;
    }
}
