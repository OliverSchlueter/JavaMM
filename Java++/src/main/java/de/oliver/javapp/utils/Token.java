package de.oliver.javapp.utils;

import java.util.ArrayList;
import java.util.List;

public enum Token {

    IDENTIFIER(null),
    CALL_FUNCTION(null),

    INTEGER(null),
    STRING(null),

    TYPE_INT("int"),
    TYPE_CHARACTER("char"),
    TYPE_STRING("string"),

    EQUALS("="),
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/");


    private final String str;

    Token(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public static List<Token> withStr(){
        List<Token> tokens = new ArrayList<>();
        for (Token t : values()) {
            if(t.getStr() != null){
                tokens.add(t);
            }
        }

        return tokens;
    }
}