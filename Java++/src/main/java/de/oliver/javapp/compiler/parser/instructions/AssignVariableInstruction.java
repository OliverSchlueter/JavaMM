package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.parser.Instruction;
import de.oliver.javapp.compiler.parser.Program;
import de.oliver.javapp.compiler.parser.Variable;
import de.oliver.javapp.exceptions.InvalidTypeException;
import de.oliver.javapp.exceptions.VariableNotFoundException;
import de.oliver.javapp.utils.Token;

public class AssignVariableInstruction extends Instruction {

    private final String variableName;
    private final Object value;
    private final Token valueType;

    public AssignVariableInstruction(Program program, int line, String variableName, Object value, Token valueType) {
        super(program, line);
        this.variableName = variableName;
        this.value = value;
        this.valueType = valueType;
    }

    @Override
    public void execute() throws VariableNotFoundException, InvalidTypeException {
        Variable var = program.getVariable(variableName);

        if(var == null){
            throw new VariableNotFoundException(variableName);
        }

        if(var.getType() != valueType){
            throw new InvalidTypeException(var, valueType);
        }

        var.setValue(value);
    }


    public String getVariableName() {
        return variableName;
    }

    public Object getValue() {
        return value;
    }

    public Token getValueType() {
        return valueType;
    }
}
