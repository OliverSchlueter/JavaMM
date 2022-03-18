package de.oliver.javamm.exceptions;

import de.oliver.javamm.compiler.parser.Function;

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
