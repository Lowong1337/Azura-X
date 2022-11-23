package fr.ducouscous.csl.compiler;

public class LexerComponent {
    public LexerComponent(LexerType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public LexerComponent(LexerType type) {
        this.type = type;
        this.data = null;
    }

    private final LexerType type;
    private final Object data;

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return type + (data == null ? "" : (" " + data));
    }

    public boolean is(LexerType type) {
        return getType() == type;
    }

    public LexerType getType() {
        return type;
    }
}
