package de.oliver.javapp.utils;

import java.util.ArrayList;
import java.util.List;

public enum Token {

    IDENTIFIER(null),
    CALL_FUNCTION(null),

    // Literals
    LITERAL_LONG(null),
    LITERAL_INTEGER(null),
    LITERAL_DOUBLE(null),
    LITERAL_FLOAT(null),
    LITERAL_STRING(null),
    LITERAL_CHARACTER(null),
    LITERAL_BOOLEAN(null),

    // Basic data types
    TYPE_LONG("long"),
    TYPE_INTEGER("int"),
    TYPE_DOUBLE("double"),
    TYPE_FLOAT("float"),
    TYPE_STRING("string"),
    TYPE_CHARACTER("char"),
    TYPE_BOOLEAN("boolean"),

    // Arithmetic operators
    PLUS("+"),
    MINUS("-"),
    STAR("*"), // multiply,
    DOUBLE_STAR("**"), // exponentiation
    SLASH("/"), // divide
    DOUBLE_SLASH("//"), // floor divide
    PERCENTAGE("%"), // modulo
    PLUS_PLUS("++"), // increment
    MINUS_MINUS("--"), // decrement

    OPEN_PARENTHESIS("("),
    END_PARENTHESIS(")"),

    //Assignment operators
    EQUALS("="),
    PLUS_EQUALS("+="),
    MINUS_EQUALS("-="),
    STAR_EQUALS("*="),
    SLASH_EQUALS("/="),


    // TODO: add Comparison Operators
    ;


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

    public static List<Token> withoutStr(){
        List<Token> tokens = new ArrayList<>();
        for (Token t : values()) {
            if(t.getStr() == null){
                tokens.add(t);
            }
        }

        return tokens;
    }
}