package de.oliver.javapp.compiler.parserUtils;

public abstract class Instruction {

    protected final Program program;
    protected final int line;

    public Instruction(Program program, int line) {
        this.program = program;
        this.line = line;
    }

    public abstract void execute();

    public int getLine() {
        return line;
    }

    public Program getProgram() {
        return program;
    }
}
