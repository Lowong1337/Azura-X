package fr.ducouscous.csl.compiler.components;

import fr.ducouscous.csl.compiler.Keyword;

import java.util.Arrays;

public class CompileBlock extends CompileComponent {
    private final Keyword keyword;
    private final CompileComponent[] components;

    public CompileComponent[] getComponents() {
        return components;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public CompileBlock(Keyword keyword, CompileComponent[] components) {
        this.keyword = keyword;
        this.components = components;
    }

    @Override
    public String toString() {
        return getKeyword().name() + " " + Arrays.toString(getComponents());
    }
}
