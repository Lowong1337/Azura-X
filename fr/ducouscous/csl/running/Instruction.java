package fr.ducouscous.csl.running;

import java.util.Arrays;

public class Instruction {
    private final Object[] data;
    final int index;

    public Object[] getData() {
        return data;
    }

    public static final int LABEL = 0;
    public static final int STORE = 1;
    public static final int GET = 2;
    public static final int GLOBAL = 3;
    public static final int SET = 4;
    public static final int ARG = 5;
    public static final int DUP = 6;
    public static final int CALL = 7;
    public static final int LDC = 8;
    public static final int LOAD = 9;
    public static final int RETURN = 10;
    public static final int GOTO = 11;
    public static final int ADD = 12;
    public static final int SUBTRACT = 13;
    public static final int DIVIDE = 14;
    public static final int MULTIPLY = 15;
    public static final int I_OR = 16;
    public static final int I_XOR = 17;
    public static final int I_AND = 18;
    public static final int BITWISE_RIGHT = 19;
    public static final int BITWISE_LEFT = 20;
    public static final int POP = 21;
    public static final int NOT = 22;
    public static final int AND = 23;
    public static final int OR = 24;
    public static final int IF = 25;
    public static final int EQUALS = 26;
    public static final int NULL = 27;
    public static final int ARRAY_PUSH = 28;
    public static final int ARRAY_GET = 29;
    public static final int ARRAY_SET = 30;
    public static final int GREATER = 31;
    public static final int LESSER = 32;
    public static final int GREATER_EQUALS = 33;
    public static final int LESSER_EQUALS = 34;
    public static final int ARRAY_LENGTH = 35;
    public static final int TABLE_GET = 36;
    public static final int TABLE_SET = 37;
    public static final int CONCAT = 38;
    public static final int MODULO = 39;
    public static final int DECREMENT = 40;
    public static final int INCREMENT = 41;
    public static final int TABLE_SIZE = 42;

    public static final String[] NAMES = new String[] {
            "LABEL",
            "STORE",
            "GET",
            "GLOBAL",
            "SET",
            "ARG",
            "DUP",
            "CALL",
            "LDC",
            "LOAD",
            "RETURN",
            "GOTO",
            "ADD",
            "SUBTRACT",
            "DIVIDE",
            "MULTIPLY",
            "I_OR",
            "I_XOR",
            "I_AND",
            "BITWISE_RIGHT",
            "BITWISE_LEFT",
            "POP",
            "NOT",
            "AND",
            "OR",
            "IF",
            "EQUALS",
            "NULL",
            "ARRAY_PUSH",
            "ARRAY_GET",
            "ARRAY_SET",
            "GREATER",
            "LESSER",
            "GREATER_EQUALS",
            "LESSER_EQUALS",
            "ARRAY_LENGTH",
            "TABLE_GET",
            "TABLE_SET",
            "CONCAT",
            "MODULO",
            "DECREMENT",
            "INCREMENT",
            "TABLE_SIZE"
    };

    public static final int LDC_STRING = 0;
    public static final int LDC_NUMBER = 1;
    public static final int LDC_BOOL = 2;
    public static final int LDC_TABLE = 3;

    public static final String[] LDC_NAMES = new String[] {
            "LDC_STRING",
            "LDC_NUMBER",
            "LDC_BOOL",
            "LDC_TABLE"
    };

    public int getIndex() {
        return index;
    }

    public Object getData(int index) {
        return data[index];
    }

    public Instruction(int index, Object...data) {
        this.data = data;
        this.index = index;
    }

    @Override
    public String toString() {
        if (index == LDC) {
            return "Instruction{" +
                    "index=" + NAMES[index] +
                    ", type=" + LDC_NAMES[(int) data[0]] +
                    ", data=" + data[1] +
                    '}';
        }
        return "Instruction{" +
                "index=" + NAMES[index] +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
