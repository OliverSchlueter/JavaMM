package de.oliver.javapp.compiler.parser;

import de.oliver.javapp.exceptions.InvalidArgumentLengthException;
import de.oliver.javapp.exceptions.VariableNotFoundException;

public abstract class Instruction {

    protected final Program program;
    protected final int line;

    public Instruction(Program program, int line) {
        this.program = program;
        this.line = line;
    }

    public abstract void execute() throws VariableNotFoundException, InvalidArgumentLengthException;

    public int getLine() {
        return line;
    }

    public Program getProgram() {
        return program;
    }
}
