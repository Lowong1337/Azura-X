package fr.ducouscous.csl.compiler.math;

import fr.ducouscous.csl.compiler.LexerComponent;

import java.util.Arrays;

public class MathCode extends MathComponent {
    private final LexerComponent[] components;
    public MathCode(LexerComponent...components) {
        this.components = components;
    }

    public LexerComponent[] getComponents() {
        return components;
    }

    @Override
    public String toString() {
        return "MathCode{" +
                "components=" + Arrays.toString(components) +
                '}';
    }
}
