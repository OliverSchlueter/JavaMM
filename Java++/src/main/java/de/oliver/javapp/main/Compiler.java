package de.oliver.javapp.main;

import de.oliver.javapp.exceptions.ForbiddenSymbolException;
import de.oliver.javapp.exceptions.NotImplementedException;
import de.oliver.javapp.utils.KeyValue;
import de.oliver.javapp.utils.Token;
import de.oliver.javapp.utils.Word;
import de.oliver.logger.LogLevel;
import de.oliver.logger.Logger;

import java.io.*;
import java.util.*;

public class Compiler {

    private final File src;
    private final Map<Integer, LinkedList<Word>> words; // line, words

    public Compiler(String srcPath) {
        this.src = new File(srcPath);
        this.words = new HashMap<>();
    }

    private void readSrc() {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(src));
        } catch (FileNotFoundException e) {
            Logger.logger.log(LogLevel.ERROR, "Could not find file: " + src.getPath());
            return;
        }

        String line = null;
        int lineIndex = 0;
        while(true) {
            try { line = br.readLine(); } catch (IOException ignored) { }
            lineIndex++;

            // end of file
            if (line == null) {
                break;
            }

            // line is empty
            if (line.isEmpty()) {
                continue;
            }

            // is a comment
            if(line.startsWith("//")){
                continue;
            }

            char[] chars = line.toCharArray();

            if (!line.startsWith(" ")) {
                String word = "";
                for (char c : chars) {
                    if (c != ' ') {
                        word += c;
                    } else {
                        break;
                    }
                }


                addWordToLine(lineIndex, new Word(lineIndex, 1, word));
            }


            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];

                if (c == ' ') {
                    String word = "";
                    boolean isString = false;

                    for (int j = i + 1; j < chars.length; j++) {

                        if(!isString && chars[j] == '"'){
                            isString = true;
                        } else if(isString && chars[j] == '"'){
                            isString = false;
                        }

                        if(isString){
                            word += chars[j];
                            i += j;
                        } else if (chars[j] != ' ') {
                            word += chars[j];
                        } else {
                            break;
                        }
                    }

                    if (!word.equals("")) {
                        addWordToLine(lineIndex, new Word(lineIndex, i + 2, word));
                    }
                }
            }
        }
    }

    public void simulate(){
        readSrc();

        Tokenizer tokenizer = new Tokenizer(words);
        Map<Integer, LinkedList<KeyValue<Word, Token>>> tokens;
        try {
            tokens = tokenizer.tokenize();
        } catch (ForbiddenSymbolException e){
            Logger.logger.log(LogLevel.ERROR, "Tokenizing failed.");
            e.printStackTrace();
            return;
        }

        tokenizer.printTokens();

    }

    public void compile() throws NotImplementedException {
        throw new NotImplementedException();
    }


    public File getSrc() {
        return src;
    }

    public Map<Integer, LinkedList<Word>> getWords() {
        return words;
    }

    private void addWordToLine(int line, Word word){
        if(words.containsKey(line)){
            words.get(line).add(word);
        } else {
            LinkedList<Word> temp = new LinkedList<>();
            temp.add(word);
            words.put(line, temp);
        }
    }

    /**
     * For debugging
     */
    public void printWords(){
        for (Map.Entry<Integer, LinkedList<Word>> entry : words.entrySet()) {
            int line = entry.getKey();
            LinkedList<Word> words = entry.getValue();

            System.out.println("----- " + line + " -----");
            for (Word word : words) {
                System.out.println(word.formattedPosition() + ": " + word.value());
            }

        }
    }
}
