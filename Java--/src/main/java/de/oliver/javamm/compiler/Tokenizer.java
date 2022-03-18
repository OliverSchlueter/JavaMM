package de.oliver.javamm.compiler;

import de.oliver.javamm.exceptions.ForbiddenSymbolException;
import de.oliver.javamm.exceptions.NotImplementedException;
import de.oliver.javamm.utils.KeyValue;
import de.oliver.javamm.utils.Token;
import de.oliver.javamm.utils.Word;

import java.util.*;

public class Tokenizer {

    private final Map<Integer, LinkedList<Word>> words;
    private final Map<Integer, LinkedList<KeyValue<Word, Token>>> tokens;

    public Tokenizer(Map<Integer, LinkedList<Word>> words) {
        this.words = words;
        this.tokens = new HashMap<>();
    }

    public Map<Integer, LinkedList<KeyValue<Word, Token>>> tokenize() throws ForbiddenSymbolException, NotImplementedException {

        for (Map.Entry<Integer, LinkedList<Word>> entry : words.entrySet()) {
            int line = entry.getKey();
            LinkedList<Word> wordList = entry.getValue();

            wordLoop:
            for (Word word : wordList) {
                for (Token token : Token.withStr()) {
                    if(word.value().equals(token.getStr())){
                        addToken(line, word, token);
                        continue wordLoop;
                    }
                }

                if(Token.withoutStr().size() != 8){
                    throw new NotImplementedException("One token is not being handled here");
                }

                if(word.value().equals("false") || word.value().equals("true")){
                    addToken(line, word, Token.LITERAL_BOOLEAN);
                    continue;
                }

                if(isInteger(word.value())){
                    addToken(line, word, Token.LITERAL_INTEGER);
                    continue;
                }

                if(isLong(word.value())){
                    addToken(line, word, Token.LITERAL_LONG);
                    continue;
                }

                if(isFloat(word.value())){
                    addToken(line, word, Token.LITERAL_FLOAT);
                    continue;
                }

                if(isDouble(word.value())){
                    addToken(line, word, Token.LITERAL_DOUBLE);
                    continue;
                }

                List<String> forbiddenCharsInIdentifier = Arrays.asList(
                        "\"", "(", ")", "[", "]", "{", "}", "=", "+", "-", "*", "/", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
                );

                // deprecated
                /*if(word.value().contains("(") && word.value().endsWith(")")){
                    String functionName = word.value().substring(0, word.value().indexOf("("));
                    for (String s : forbiddenCharsInIdentifier) {
                        if(functionName.contains(s)){
                            throw new ForbiddenSymbolException(word, s);
                        }
                    }
                    addToken(line, word, Token.CALL_FUNCTION);
                    continue;
                }*/

                if(word.value().startsWith("\"") && word.value().endsWith("\"")){
                    addToken(line, word, Token.LITERAL_STRING);
                    continue;
                }

                if(word.value().startsWith("'") && word.value().endsWith("'") && word.value().length() == 3){
                    addToken(line, word, Token.LITERAL_CHARACTER);
                    continue;
                }


                for (String s : forbiddenCharsInIdentifier) {
                    if(word.value().contains(s)){
                        throw new ForbiddenSymbolException(word, s);
                    }
                }

                addToken(line, word, Token.IDENTIFIER);
            }
        }

        return tokens;
    }

    private boolean isLong(String str){
        try { Long.parseLong(str); }
        catch (NumberFormatException e){ return false; }
        return true;
    }

    private boolean isInteger(String str){
        try { Integer.parseInt(str); }
        catch (NumberFormatException e){ return false; }
        return true;
    }

    private boolean isDouble(String str){
        try { Double.parseDouble(str); }
        catch (NumberFormatException e){ return false; }
        return true;
    }

    private boolean isFloat(String str){
        try { Float.parseFloat(str); }
        catch (NumberFormatException e){ return false; }
        return true;
    }

    private void addToken(int line, Word word, Token token){

        if(tokens.containsKey(line)){
            LinkedList<KeyValue<Word, Token>> lineWords = this.tokens.get(line);
            lineWords.add(new KeyValue<>(word, token));
        } else {
            LinkedList<KeyValue<Word, Token>> lineWords = new LinkedList<>();
            lineWords.add(new KeyValue<>(word, token));
            tokens.put(line, lineWords);
        }
    }

    /**
     * For debugging
     */
    public void printTokens(){
        for (Map.Entry<Integer, LinkedList<KeyValue<Word, Token>>> entry : tokens.entrySet()) {
            int line = entry.getKey();
            LinkedList<KeyValue<Word, Token>> lineTokens = entry.getValue();

            System.out.println("----- " + line + " -----");
            for (KeyValue<Word, Token> wordToken : lineTokens) {
                Word word = wordToken.getKey();
                Token token = wordToken.getValue();

                System.out.println(word.formattedPosition() + ": " + word.value() + " : " + token);
            }

        }
    }

}
