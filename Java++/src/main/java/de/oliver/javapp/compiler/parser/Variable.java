package de.oliver.javapp.compiler.parser;

import de.oliver.javapp.utils.Token;

public class Variable {

    private final String name;
    private final Token type;
    private Object value;

    public Variable(String name, Token type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Token getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
