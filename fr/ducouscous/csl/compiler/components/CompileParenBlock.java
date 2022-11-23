package fr.ducouscous.csl.compiler.components;

import fr.ducouscous.csl.compiler.Keyword;
import fr.ducouscous.csl.compiler.LexerComponent;

import java.util.Arrays;

public class CompileParenBlock extends CompileBlock {
    private final LexerComponent[] paren;

    public LexerComponent[] getParen() {
        return paren;
    }

    public CompileParenBlock(Keyword keyword, CompileComponent[] components, LexerComponent[] paren) {
        super(keyword, components);
        this.paren = paren;
    }

    @Override
    public String toString() {
        return getKeyword().name() + " " + Arrays.toString(getParen()) + " " + Arrays.toString(getComponents());
    }
}
