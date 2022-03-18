package de.oliver.javapp.exceptions;

import de.oliver.javapp.compiler.parser.Function;

public class NoReturnException extends Exception{

    private final Function function;

    public NoReturnException(Function function) {
        super("No return set for function: '" + function.getName() + "'");
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }
}
