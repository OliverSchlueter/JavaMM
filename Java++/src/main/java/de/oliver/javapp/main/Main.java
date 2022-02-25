package de.oliver.javapp.main;

import de.oliver.logger.LogLevel;
import de.oliver.logger.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        File inputFile = new File("D:\\Workspaces\\IntelliJ\\JavaPP\\TestCode\\main.jpp");
        BufferedReader br = new BufferedReader(new FileReader(inputFile));

        String line;
        int lineIndex = 0;
        while(true){
            line = br.readLine();
            lineIndex++;

            // end of file
            if(line == null){
                break;
            }

            // line is empty
            if(line.isEmpty()){
                continue;
            }


            List<Word> words = new ArrayList<>();

            char[] chars = line.toCharArray();

            if(!line.startsWith(" ")){
                String word = "";
                for (char c : chars) {
                    if (c != ' ') {
                        word += c;
                    } else {
                        break;
                    }
                }

                words.add(new Word(1, word));
            }

            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if(c == ' '){
                    String word = "";
                    for (int j = i+1; j < chars.length; j++) {
                        if(chars[j] != ' '){
                            word += chars[j];
                        } else {
                            break;
                        }
                    }

                    if(!word.equals("")){
                        words.add(new Word(i + 2, word));
                    }
                }
            }

            System.out.println("Line: " + lineIndex);

            for (Word word : words) {
                System.out.println(word.index() + ":'" + word.value() + "'");
            }

        }

    }

}
