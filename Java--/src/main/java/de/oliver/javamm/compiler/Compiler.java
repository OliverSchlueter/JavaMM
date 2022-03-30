package de.oliver.javamm.compiler;

import de.oliver.javamm.compiler.parser.Program;
import de.oliver.javamm.exceptions.*;
import de.oliver.javamm.utils.KeyValue;
import de.oliver.javamm.utils.Token;
import de.oliver.javamm.utils.Word;
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

    /**
     * Generates ´words´ out of characters in the src-file
     */
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

            line = line.replace("\t", "");

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

                for (int i = 0; i < chars.length; i++) {
                    char c = chars[i];

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
                    boolean currentWordIsString = false;
                    boolean isString = false;

                    for (int j = i + 1; j < chars.length; j++) {

                        if(!isString && chars[j] == '"'){
                            isString = true;
                            currentWordIsString = true;
                        } else if(isString && chars[j] == '"'){
                            isString = false;
                        }

                        if(isString){
                            word += chars[j];
                            i += j-i;
                        } else if (chars[j] != ' ') {
                            word += chars[j];
                        } else {
                            break;
                        }
                    }

                    if (!word.equals("")) {
                        if(currentWordIsString){
                            word = word.replace("\\n", "\n");
                        }
                        addWordToLine(lineIndex, new Word(lineIndex, i + 2, word));
                    }
                }
            }
        }
    }

    /**
     * Simulate the program
     */
    public void simulate() {
        long startTime = System.currentTimeMillis();

        readSrc();

        long timeReadSrc = System.currentTimeMillis() - startTime;
        Logger.logger.log(Compiler.class, LogLevel.INFO, "Reading src took " + timeReadSrc + "ms");
        long startTimeTokenize = System.currentTimeMillis();

        Tokenizer tokenizer = new Tokenizer(words);
        Map<Integer, LinkedList<KeyValue<Word, Token>>> tokens;
        try {
            tokens = tokenizer.tokenize();
        } catch (NotImplementedException | ForbiddenSymbolException e){
            Logger.logger.log(Compiler.class, LogLevel.ERROR, "Tokenizing failed.");
            e.printStackTrace();
            return;
        }

        long timeTokenize = System.currentTimeMillis() - startTimeTokenize;
        Logger.logger.log(Compiler.class, LogLevel.INFO, "Tokenizing took " + timeTokenize + "ms");
        long startTimeParsing = System.currentTimeMillis();

        //tokenizer.printTokens();

        Parser parser = new Parser(tokens);
        Program program;
        try {
            program = parser.generateProgram();
        } catch (VariableNotFoundException | InvalidArgumentLengthException | FunctionNotFoundException | VariableAlreadyExistsException | InvalidTypeException | InvalidOperatorException | NoReturnException | FunctionInFunctionException e){
            Logger.logger.log(Compiler.class, LogLevel.ERROR, "Parsing failed.");
            e.printStackTrace();
            return;
        }
        long timeParsing = System.currentTimeMillis() - startTimeParsing;
        Logger.logger.log(Compiler.class, LogLevel.INFO, "Parsing took " + timeParsing + "ms");

        long totalTime = System.currentTimeMillis() - startTime;
        Logger.logger.log(Compiler.class, LogLevel.INFO, "Generating simulation took " + totalTime + "ms\n");

        try {
            program.run(new LinkedList<>());
        } catch (VariableNotFoundException | InvalidArgumentLengthException | FunctionNotFoundException | VariableAlreadyExistsException | InvalidTypeException | NoReturnException e){
            Logger.logger.log(Compiler.class, LogLevel.ERROR, "Executing failed.");
            e.printStackTrace();
            return;
        }

    }

    /**
     * Compiles the program into ____
     */
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
