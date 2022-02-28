package de.oliver.javapp.compiler.parserUtils;

public class AssignVariableInstruction extends Instruction{

    private final String variableName;
    private final Object value;

    public AssignVariableInstruction(Program program, int line, String variableName, Object value) {
        super(program, line);
        this.variableName = variableName;
        this.value = value;
    }

    @Override
    public void execute() {
        Variable var = program.getVariable(variableName); //TODO: check if variable null
        var.setValue(value);
    }


    public String getVariableName() {
        return variableName;
    }

    public Object getValue() {
        return value;
    }
}
