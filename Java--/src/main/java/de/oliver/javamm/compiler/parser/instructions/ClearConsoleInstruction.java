package de.oliver.javamm.compiler.parser.instructions;

import de.oliver.javamm.compiler.parser.Block;
import de.oliver.javamm.compiler.parser.Instruction;
import de.oliver.javamm.exceptions.*;
import de.oliver.logger.LogLevel;
import de.oliver.logger.Logger;

public class ClearConsoleInstruction extends Instruction {

    public ClearConsoleInstruction(Block parentBlock, Block block, int line) {
        super(parentBlock, block, line);
    }

    @Override
    public void execute() throws VariableNotFoundException, InvalidArgumentLengthException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException, NoReturnException {
        Logger.logger.log(ClearConsoleInstruction.class, LogLevel.INFO, "Clearing console");
        for (int i = 0; i < 1000; i++) {
            System.out.println("");
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
