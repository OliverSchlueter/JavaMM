package de.oliver.javapp.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Token {

    IDENTIFIER(null),

    DEF("def"),
    RETURN("return"),
    VOID("void"),

    // Literals
    LITERAL_LONG(null),
    LITERAL_INTEGER(null),
    LITERAL_DOUBLE(null),
    LITERAL_FLOAT(null),
    LITERAL_STRING(null),
    LITERAL_CHARACTER(null),
    LITERAL_BOOLEAN(null),

    // Basic data types
    TYPE_OBJECT("object"),
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
    SLASH("/"), // divide
    DOUBLE_STAR("**"), // exponentiation TODO: not implement
    DOUBLE_SLASH("//"), // floor divide TODO: not implement
    PERCENTAGE("%"), // modulo TODO: not implement
    PLUS_PLUS("++"), // increment TODO: not implement
    MINUS_MINUS("--"), // decrement TODO: not implement

    OPEN_PARENTHESIS("("),
    CLOSE_PARENTHESIS(")"),
    OPEN_BRACES("{"),
    CLOSE_BRACES("}"),
    COMMA(","),

    //Assignment operators
    EQUALS("="),
    PLUS_EQUALS("+="), // TODO: not implement
    MINUS_EQUALS("-="), // TODO: not implement
    STAR_EQUALS("*="), // TODO: not implement
    SLASH_EQUALS("/="), // TODO: not implement


    // TODO: add Comparison Operators
    ;


    private final String str;

    Token(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public static List<Token> basicDataTypes() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(VOID);
        for (Token value : values()) {
            if(value.name().startsWith("TYPE_")){
                tokens.add(value);
            }
        }
        return tokens;
    }

    public static List<Token> arithmeticOperators(){
        return Arrays.asList(PLUS, MINUS, STAR, SLASH);
    }

    public static List<Token> literals() {
        List<Token> tokens = new ArrayList<>();
        for (Token value : values()) {
            if(value.name().startsWith("LITERAL_")){
                tokens.add(value);
            }
        }
        return tokens;
    }

    public static Token getTypeOfLiteral(Token literal){
        switch (literal){
            case LITERAL_STRING -> { return Token.TYPE_STRING; }
            case LITERAL_BOOLEAN -> { return Token.TYPE_BOOLEAN; }
            case LITERAL_CHARACTER -> { return Token.TYPE_CHARACTER; }
            case LITERAL_DOUBLE -> { return Token.TYPE_DOUBLE; }
            case LITERAL_FLOAT -> { return Token.TYPE_FLOAT; }
            case LITERAL_LONG -> { return Token.TYPE_LONG; }
            case LITERAL_INTEGER -> { return Token.TYPE_INTEGER; }
        }

        return null;
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