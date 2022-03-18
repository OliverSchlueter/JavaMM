package de.oliver.javamm.utils;

public record Word(int line, int column, String value) {

    public String formattedPosition(){
        return "[" + line + ":" + column + "]";
    }

}
