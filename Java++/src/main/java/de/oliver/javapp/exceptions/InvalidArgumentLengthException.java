package de.oliver.javapp.exceptions;

public class InvalidArgumentLengthException extends Exception{

    private final int expected;
    private final int provided;

    public InvalidArgumentLengthException(int expected, int provided) {
        super("Invalid argument length. " + expected + " expected, " + provided + " provided");
        this.expected = expected;
        this.provided = provided;
    }

    public int getExpected() {
        return expected;
    }


    public int getProvided() {
        return provided;
    }
}
