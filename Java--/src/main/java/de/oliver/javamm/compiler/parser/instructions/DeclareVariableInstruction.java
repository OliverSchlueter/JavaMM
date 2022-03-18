package de.oliver.javamm.compiler.parser.instructions;

import de.oliver.javamm.compiler.Parser;
import de.oliver.javamm.compiler.parser.Block;
import de.oliver.javamm.compiler.parser.Instruction;
import de.oliver.javamm.compiler.parser.Variable;
import de.oliver.javamm.exceptions.InvalidTypeException;
import de.oliver.javamm.exceptions.VariableAlreadyExistsException;
import de.oliver.javamm.exceptions.VariableNotFoundException;
import de.oliver.javamm.utils.KeyValue;
import de.oliver.javamm.utils.Node;
import de.oliver.javamm.utils.Token;
import de.oliver.javamm.utils.Word;

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
            case TYPE_FLOAT -> value = (float) Parser.calculateAst(block, ast);
            case TYPE_LONG -> value = (long) Parser.calculateAst(block, ast);
            case TYPE_INTEGER -> value = (int) Parser.calculateAst(block, ast);
            default -> value = Parser.calculateAst(block, ast);
        }

        value = Parser.castToType(value, type);

        Variable var = new Variable(identifier.value(), type, value);

        if(block.getVariable(identifier.value()) != null && block.getVariable(identifier.value()).getValue() != null){
            throw new VariableAlreadyExistsException(identifier.value());
        }

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
