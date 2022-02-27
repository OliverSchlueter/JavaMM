package de.oliver.javapp.utils;

public record Word(int line, int column, String value) {

    public String formattedPosition(){
        return "[" + line + ":" + column + "]";
    }

}
