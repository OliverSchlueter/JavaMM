package de.oliver.javamm.exceptions;

import de.oliver.javamm.utils.Token;

public class InvalidOperatorException extends Exception{

    private final Token operator;

    public InvalidOperatorException(Token operator) {
        super("Invalid operator: " + operator);
        this.operator = operator;
    }

    public Token getOperator() {
        return operator;
    }
}
