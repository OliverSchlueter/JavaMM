package de.oliver.javapp.exceptions;

import de.oliver.javapp.utils.Word;

public class ForbiddenSymbolException extends Exception {

    private final Word word;
    private final String symbol;

    public ForbiddenSymbolException(Word word, String symbol) {
        super("Forbidden symbol found at [line:" + word.line() + " | column:" + word.column() + "]. Symbol: '" + symbol + "'");
        this.word = word;
        this.symbol = symbol;
    }

    public Word getWord() {
        return word;
    }

    public String getSymbol() {
        return symbol;
    }
}
