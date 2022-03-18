package de.oliver.javapp.exceptions;

public class FunctionInFunctionException extends Exception{

    public FunctionInFunctionException() {
        super("Creating a function in a function is not allowed");
    }
}
