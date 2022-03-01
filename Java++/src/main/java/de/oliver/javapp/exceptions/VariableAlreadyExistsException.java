package de.oliver.javapp.exceptions;

public class VariableAlreadyExistsException extends Exception{

    private final String variableName;

    public VariableAlreadyExistsException(String variableName) {
        super("There already exists a variable with the name: '" + variableName + "'");
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }
}
