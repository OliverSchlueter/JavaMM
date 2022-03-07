package de.oliver.javapp.compiler.parser;

import java.util.LinkedList;

public abstract class DefineBlockInstruction extends Instruction {

    protected final LinkedList<Instruction> instructions;

    public DefineBlockInstruction(Program program, int line, LinkedList<Instruction> instructions) {
        super(program, line);
        this.instructions = instructions;
    }

    public LinkedList<Instruction> getInstructions() {
        return instructions;
    }
    public void addInstruction(Instruction instruction){
        this.instructions.add(instruction);
    }
}
