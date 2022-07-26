package de.oliver.javamm.compiler.parser.instructions;

import de.oliver.javamm.compiler.parser.Block;
import de.oliver.javamm.compiler.parser.Instruction;
import de.oliver.javamm.exceptions.*;
import de.oliver.javamm.utils.Word;

public class DeclareBookmarkInstruction extends Instruction {

    private final String name;

    public DeclareBookmarkInstruction(Block parentBlock, Block block, int line, String name) {
        super(parentBlock, block, line);
        this.name = name;
    }

    @Override
    public void execute() throws VariableNotFoundException, InvalidArgumentLengthException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException, NoReturnException, BookmarkAlreadyExistsException {
        block.addBookmark(name, line);
    }

    public String getName() {
        return name;
    }
}
