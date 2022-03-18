package de.oliver.javamm.exceptions;

public class NotImplementedException extends Exception{

    public NotImplementedException() {
        super("This feature is not implemented yet.");
    }

    public NotImplementedException(String message){
        super(message);
    }
}
