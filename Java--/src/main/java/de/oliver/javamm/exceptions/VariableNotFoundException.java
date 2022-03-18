package de.oliver.javamm.exceptions;

public class VariableNotFoundException extends Exception{

    private final String variableName;

    public VariableNotFoundException(String variableName) {
        super("Cloud not find variable: '" + variableName + "'");
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }
}
