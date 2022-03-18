package de.oliver.javamm.compiler.parser.instructions;

import de.oliver.javamm.compiler.Parser;
import de.oliver.javamm.compiler.parser.Block;
import de.oliver.javamm.compiler.parser.Instruction;
import de.oliver.javamm.compiler.parser.Variable;
import de.oliver.javamm.exceptions.*;
import de.oliver.javamm.utils.Token;

import java.util.ArrayList;
import java.util.List;

public class IncrementInstruction extends Instruction {

    private static List<Token> allowedTypes;
    static {
        allowedTypes = new ArrayList<>();
        allowedTypes.add(Token.TYPE_INTEGER);
        allowedTypes.add(Token.TYPE_LONG);
        allowedTypes.add(Token.TYPE_FLOAT);
        allowedTypes.add(Token.TYPE_DOUBLE);
    }

    private final String varName;
    private final Token type;

    public IncrementInstruction(Block parentBlock, Block block, int line, String varName, Token type) {
        super(parentBlock, block, line);
        this.varName = varName;
        this.type = type;
    }

    @Override
    public void execute() throws VariableNotFoundException, InvalidArgumentLengthException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {
        Variable var = block.getVariable(varName);

        if(var == null){
            throw new VariableNotFoundException(varName);
        }

        if(var.getType() != type && type != null){
            throw new InvalidTypeException(var, line, type);
        }

        if(!allowedTypes.contains(type)){
            throw new InvalidTypeException(var, line, Token.TYPE_NUMBER);
        }

        if(type == Token.TYPE_DOUBLE || type == Token.TYPE_FLOAT){
            double value = Double.parseDouble(var.getValue().toString());
            value++;
            var.setValue(Parser.castToType(value, type));
        } else if(type == Token.TYPE_LONG || type == Token.TYPE_INTEGER){
            long value = Long.parseLong(var.getValue().toString());
            value++;
            var.setValue(Parser.castToType(value, type));
        }
    }

    public String getVarName() {
        return varName;
    }

    public Token getType() {
        return type;
    }
}
