package de.oliver.javapp.exceptions;

import de.oliver.javapp.compiler.parser.Variable;
import de.oliver.javapp.utils.Token;

public class InvalidTypeException extends Exception{

    private final Variable variable;

    public InvalidTypeException(Variable variable, int line, Token expected) {
        super("Wrong type for variable '" + variable.getName() + "' at line: " + line + ". Expected: " + expected + " provided: " + variable.getType());
        this.variable = variable;
    }

    public Variable getVariable() {
        return variable;
    }

}
