package de.oliver.javamm.exceptions;

public class FunctionInFunctionException extends Exception{

    public FunctionInFunctionException() {
        super("Creating a function in a function is not allowed");
    }
}
