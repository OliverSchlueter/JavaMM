package de.oliver.javamm.exceptions;

public class BookmarkAlreadyExistsException extends Exception{

    private final String name;

    public BookmarkAlreadyExistsException(String name) {
        super("Bookmark '" + name + "' is already declared");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
