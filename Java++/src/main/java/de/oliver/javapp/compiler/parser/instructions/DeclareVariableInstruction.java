package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.Parser;
import de.oliver.javapp.compiler.parser.Block;
import de.oliver.javapp.compiler.parser.Instruction;
import de.oliver.javapp.compiler.parser.Variable;
import de.oliver.javapp.exceptions.VariableAlreadyExistsException;
import de.oliver.javapp.utils.KeyValue;
import de.oliver.javapp.utils.Node;
import de.oliver.javapp.utils.Token;
import de.oliver.javapp.utils.Word;

public class DeclareVariableInstruction extends Instruction {

    private final Word identifier;
    private final Token type;
    private final Node<KeyValue<Word, Token>> ast;

    public DeclareVariableInstruction(Block parentBlock, Block block, int line, Word identifier, Token type, Node<KeyValue<Word, Token>> ast) {
        super(parentBlock, block, line);
        this.identifier = identifier;
        this.type = type;
        this.ast = ast;
    }

    @Override
    public void execute() throws VariableAlreadyExistsException {
        Variable var = new Variable(identifier.value(), type, Parser.calculateAst(block, ast));

        if(block.getVariable(identifier.value()) != null){
            throw new VariableAlreadyExistsException(identifier.value());
        }

        // TODO: check if actual value is matching type

        block.addVariable(var);
    }

    public Word getIdentifier() {
        return identifier;
    }

    public Token getType() {
        return type;
    }

    public Node<KeyValue<Word, Token>> getAst() {
        return ast;
    }
}
