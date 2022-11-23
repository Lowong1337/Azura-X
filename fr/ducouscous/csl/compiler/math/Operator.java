package fr.ducouscous.csl.compiler.math;

import fr.ducouscous.csl.running.Instruction;

public enum Operator {
    ADD(false, new Instruction(Instruction.ADD)),
    MULTIPLY(false, true, new Instruction(Instruction.MULTIPLY)),
    DIVIDE(false, true, new Instruction(Instruction.DIVIDE)),
    BITWISE_LEFT(false, new Instruction(Instruction.BITWISE_LEFT)),
    BITWISE_RIGHT(false, new Instruction(Instruction.BITWISE_RIGHT)),
    AND(false, new Instruction(Instruction.I_AND)),
    OR(false, new Instruction(Instruction.I_OR)),
    XOR(false, new Instruction(Instruction.I_XOR)),
    CONCAT(false, new Instruction(Instruction.CONCAT)),
    SUBTRACT(false, new Instruction(Instruction.SUBTRACT)),
    BOOLEAN_OR(true, new Instruction(Instruction.OR)),
    BOOLEAN_XOR(true, new Instruction(Instruction.NOT), new Instruction(Instruction.EQUALS)),
    GREATER_EQUALS(true, new Instruction(Instruction.GREATER_EQUALS)),
    LESSER_EQUALS(true, new Instruction(Instruction.LESSER_EQUALS)),
    GREATER(true, new Instruction(Instruction.GREATER)),
    LESSER(true, new Instruction(Instruction.LESSER)),
    EQUALS(true, new Instruction(Instruction.EQUALS)),
    BOOLEAN_AND(true, new Instruction(Instruction.AND)),
    MODULO(false, new Instruction(Instruction.MODULO));
    public final boolean boolOperator, priority;
    public final Instruction[] instructions;
    Operator(boolean boolOperator, boolean priority, Instruction...instructions) {
        this.instructions = instructions;
        this.boolOperator = boolOperator;
        this.priority = priority;
    }
    Operator(boolean boolOperator, Instruction...instructions) {
        this(boolOperator, false, instructions);
    }
}
