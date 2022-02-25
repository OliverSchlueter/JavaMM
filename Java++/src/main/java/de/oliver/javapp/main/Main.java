package de.oliver.javapp.main;

import de.oliver.logger.LogLevel;
import de.oliver.logger.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

            // remove whitespaces
            while(line.contains("  ")){
                line = line.replace("  ", " ");
            }

            String[] words = line.split(" ");
            for (String word : words) {
                System.out.println("word:'" + word + "'");
            }

            System.out.println(lineIndex + ":" + line);
        }

    }

}
