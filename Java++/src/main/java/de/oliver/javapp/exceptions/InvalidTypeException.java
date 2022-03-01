package de.oliver.javapp.exceptions;

import de.oliver.javapp.compiler.parser.Variable;
import de.oliver.javapp.utils.Token;

public class InvalidTypeException extends Exception{

    private final Variable variable;
    private final Token type;

    public InvalidTypeException(Variable variable, Token type) {
        super("Cannot assign variable '" + variable.getName() + "' to a value with the type: '" + type + "'");
        this.variable = variable;
        this.type = type;
    }

    public Variable getVariable() {
        return variable;
    }

    public Token getType() {
        return type;
    }
}
