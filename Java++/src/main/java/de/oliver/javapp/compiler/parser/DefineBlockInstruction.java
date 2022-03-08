package de.oliver.javapp.compiler.parser;

import java.util.LinkedList;

public abstract class DefineBlockInstruction extends Instruction {

    protected final LinkedList<Instruction> instructions;

    public DefineBlockInstruction(Block parentBlock, Block block, int line, LinkedList<Instruction> instructions) {
        super(parentBlock, block, line);
        this.instructions = instructions;
    }

    public LinkedList<Instruction> getInstructions() {
        return instructions;
    }
    public void addInstruction(Instruction instruction){
        this.instructions.add(instruction);
    }
}
