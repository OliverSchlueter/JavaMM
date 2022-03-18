package de.oliver.javamm.compiler.parser.instructions;

import de.oliver.javamm.compiler.parser.Block;
import de.oliver.javamm.compiler.parser.Instruction;
import de.oliver.javamm.exceptions.*;
import de.oliver.javamm.utils.KeyValue;
import de.oliver.javamm.utils.Node;
import de.oliver.javamm.utils.Token;
import de.oliver.javamm.utils.Word;

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
