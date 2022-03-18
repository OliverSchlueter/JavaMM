package de.oliver.javamm.exceptions;

public class FunctionNotFoundException extends Exception{

    private final String functionName;

    public FunctionNotFoundException(String functionName) {
        super("Cloud not find function with name: '" + functionName + "'");
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }
}
