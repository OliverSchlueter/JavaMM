package de.oliver.javamm.compiler.parser.instructions;

import de.oliver.javamm.compiler.parser.Block;
import de.oliver.javamm.compiler.parser.Instruction;
import de.oliver.javamm.compiler.parser.Program;
import de.oliver.javamm.exceptions.*;

public class ExitInstruction extends Instruction {

    public ExitInstruction(Block parentBlock, Block block, int line) {
        super(parentBlock, block, line);
    }

    @Override
    public void execute() throws VariableNotFoundException, InvalidArgumentLengthException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException, NoReturnException {
        ((Program) parentBlock).stopProgram();
    }
}
