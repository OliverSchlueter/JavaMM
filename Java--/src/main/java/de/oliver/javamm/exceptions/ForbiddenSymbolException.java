package de.oliver.javamm.exceptions;

import de.oliver.javamm.utils.Word;

public class ForbiddenSymbolException extends Exception {

    private final Word word;
    private final String symbol;

    public ForbiddenSymbolException(Word word, String symbol) {
        super("Forbidden symbol found at " + word.formattedPosition() + ". Symbol: '" + symbol + "'");
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
