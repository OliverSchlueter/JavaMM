package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.parser.Instruction;
import de.oliver.javapp.compiler.parser.Program;
import de.oliver.javapp.compiler.parser.Variable;
import de.oliver.javapp.exceptions.VariableAlreadyExistsException;
import de.oliver.javapp.utils.Token;
import de.oliver.javapp.utils.Word;

public class DeclareVariableInstruction extends Instruction {

    private final Word identifier;
    private final Token type;
    private final Object value;

    public DeclareVariableInstruction(Program program, int line, Word identifier, Token type, Object value) {
        super(program, line);
        this.identifier = identifier;
        this.type = type;
        this.value = value;
    }

    @Override
    public void execute() throws VariableAlreadyExistsException {
        Variable var = new Variable(identifier.value(), type, value);

        if(program.getVariable(identifier.value()) != null){
            throw new VariableAlreadyExistsException(identifier.value());
        }

        program.addVariable(var);
    }

    public Word getIdentifier() {
        return identifier;
    }

    public Token getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
