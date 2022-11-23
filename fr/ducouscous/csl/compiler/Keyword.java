package fr.ducouscous.csl.compiler;

public enum Keyword {
    FOR("for", true, true, true),
    WHILE("while", true, true, true),
    FUNCTION("function", true),
    IF("if", true, false, true),
    ELSE("else", true),
    ELIF("elif", true),
    RETURN("return"),
    BREAK("break"),
    CONTINUE("continue"),
    VAR("var"),
    IMPORT("import"),
    ;
    public final boolean block, loop, hasParen;
    public final String name;
    Keyword(String name, boolean block, boolean loop, boolean hasParen) {
        this.block = block;
        this.loop = loop;
        this.name = name;
        this.hasParen = hasParen;
    }
    Keyword(String name) {
        this(name, false, false, false);
    }
    Keyword(String name, boolean block) {
        this(name, block, false, false);
    }

    @Override
    public String toString() {
        return name() + " " + block + " " + loop;
    }
}
