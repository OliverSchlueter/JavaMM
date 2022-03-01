package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.parser.Instruction;
import de.oliver.javapp.compiler.parser.Program;
import de.oliver.javapp.compiler.parser.Variable;
import de.oliver.javapp.exceptions.VariableNotFoundException;

public class AssignVariableInstruction extends Instruction {

    private final String variableName;
    private final Object value;

    public AssignVariableInstruction(Program program, int line, String variableName, Object value) {
        super(program, line);
        this.variableName = variableName;
        this.value = value;
    }

    @Override
    public void execute() throws VariableNotFoundException {
        Variable var = program.getVariable(variableName);

        if(var == null){
            throw new VariableNotFoundException(variableName);
        }

        var.setValue(value);
    }


    public String getVariableName() {
        return variableName;
    }

    public Object getValue() {
        return value;
    }
}
