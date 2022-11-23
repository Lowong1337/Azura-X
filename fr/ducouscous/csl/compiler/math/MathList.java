package fr.ducouscous.csl.compiler.math;

import java.util.Arrays;

public class MathList extends MathComponent {
    private final MathComponent[] components;
    public MathList(MathComponent...components) {
        this.components = components;
    }

    public MathComponent[] getComponents() {
        return components;
    }

    @Override
    public String toString() {
        return "MathList{" +
                "components=" + Arrays.toString(components) +
                '}';
    }
}
