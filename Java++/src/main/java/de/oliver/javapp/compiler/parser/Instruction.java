package de.oliver.javapp.compiler.parser;

import de.oliver.javapp.exceptions.*;


public abstract class Instruction {

    protected Block block;
    protected final int line;

    public Instruction(Block parentBlock, Block block, int line) {
        this.block = block;
        this.line = line;
    }

    public abstract void execute() throws VariableNotFoundException, InvalidArgumentLengthException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException;

    public int getLine() {
        return line;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

}
