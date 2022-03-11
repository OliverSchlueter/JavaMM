package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.Parser;
import de.oliver.javapp.compiler.parser.Block;
import de.oliver.javapp.compiler.parser.Instruction;
import de.oliver.javapp.compiler.parser.Variable;
import de.oliver.javapp.exceptions.InvalidTypeException;
import de.oliver.javapp.exceptions.VariableAlreadyExistsException;
import de.oliver.javapp.exceptions.VariableNotFoundException;
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
    public void execute() throws VariableAlreadyExistsException, VariableNotFoundException, InvalidTypeException {
        Object value = null;

        switch (type){
            case TYPE_STRING -> value = Parser.calcStringAst(block, ast);
            case TYPE_DOUBLE -> value = Parser.calculateAst(block, ast);
            case TYPE_INTEGER -> value = (int) Parser.calculateAst(block, ast);
            // TODO: add all other datatypes
            default -> value = Parser.calculateAst(block, ast);
        }

        Variable var = new Variable(identifier.value(), type, value);

        if(block.getVariable(identifier.value()) != null && block.getVariable(identifier.value()).getValue() != null){
            throw new VariableAlreadyExistsException(identifier.value());
        }

        // TODO: check if actual value is matching type
        if(block.getVariable(identifier.value()) != null){
            block.getVariable(identifier.value()).setValue(var.getValue());
        } else {
            block.addVariable(var);
        }
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
