package de.oliver.javapp.exceptions;

public class NotImplementedException extends Exception{

    public NotImplementedException() {
        super("This feature is not implemented yet.");
    }

    public NotImplementedException(String message){
        super(message);
    }
}
