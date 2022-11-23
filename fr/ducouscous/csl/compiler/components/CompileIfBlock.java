package fr.ducouscous.csl.compiler.components;

public class CompileIfBlock extends CompileComponent {
    private final CompileParenBlock ifBlock;
    private final CompileBlock elseBlock;

    public CompileBlock getElseBlock() {
        return elseBlock;
    }

    public CompileParenBlock getIfBlock() {
        return ifBlock;
    }

    public CompileIfBlock(CompileParenBlock ifBlock, CompileBlock elseBlock) {
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public String toString() {
        return "(" + ifBlock.toString() + ") (" + elseBlock.toString() + ")";
    }
}
