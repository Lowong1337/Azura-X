package fr.ducouscous.csl.compiler.math;

public class MathOperator extends MathComponent {
    private final Operator operator;
    public MathOperator(Operator operator) {
        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "MathOperator{" +
                "operator=" + operator +
                '}';
    }
}
