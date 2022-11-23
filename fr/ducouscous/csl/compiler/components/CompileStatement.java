package fr.ducouscous.csl.compiler.components;

import fr.ducouscous.csl.compiler.LexerComponent;

import java.util.Arrays;

public class CompileStatement extends CompileComponent {
    LexerComponent[] components;

    public CompileStatement(LexerComponent...components) {
        this.components = components;
    }

    public LexerComponent[] getComponents() {
        return components;
    }

    @Override
    public String toString() {
        return Arrays.toString(components);
    }
}
