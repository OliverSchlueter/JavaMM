package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.Parser;
import de.oliver.javapp.compiler.parser.Block;
import de.oliver.javapp.compiler.parser.Function;
import de.oliver.javapp.compiler.parser.Instruction;
import de.oliver.javapp.exceptions.*;
import de.oliver.javapp.utils.KeyValue;
import de.oliver.javapp.utils.Node;
import de.oliver.javapp.utils.Token;
import de.oliver.javapp.utils.Word;

public class ReturnInstruction extends Instruction {

    private final Node<KeyValue<Word, Token>> ast;

    public ReturnInstruction(Block parentBlock, Block block, int line, Node<KeyValue<Word, Token>> ast) {
        super(parentBlock, block, line);
        this.ast = ast;
    }

    @Override
    public void execute() throws VariableNotFoundException, InvalidArgumentLengthException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {

    }

    public Node<KeyValue<Word, Token>> getAst() {
        return ast;
    }
}
