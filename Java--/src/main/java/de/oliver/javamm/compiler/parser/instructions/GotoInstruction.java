package de.oliver.javamm.compiler.parser.instructions;

import de.oliver.javamm.compiler.parser.Block;
import de.oliver.javamm.compiler.parser.Instruction;
import de.oliver.javamm.exceptions.*;
import de.oliver.javamm.utils.KeyValue;

public class GotoInstruction extends Instruction {

    private final String bookmarkName;
    private int bookmarkLine;

    public GotoInstruction(Block parentBlock, Block block, int line, String bookmarkName) {
        super(parentBlock, block, line);
        this.bookmarkName = bookmarkName;
    }

    @Override
    public void execute() throws VariableNotFoundException, InvalidArgumentLengthException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException, NoReturnException, BookmarkAlreadyExistsException {
        if(!block.getBookmarks().containsKey(bookmarkName)){
            // TODO: bookmark not found
            return;
        }

        bookmarkLine = block.getBookmarks().get(bookmarkName);
    }

    public String getBookmarkName() {
        return bookmarkName;
    }

    public int getBookmarkLine() {
        return bookmarkLine;
    }
}
