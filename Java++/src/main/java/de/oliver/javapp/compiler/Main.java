package de.oliver.javapp.compiler;

import de.oliver.javapp.exceptions.NotImplementedException;
import de.oliver.logger.LogLevel;
import de.oliver.logger.Logger;

public class Main {

    public static void main(String[] args) throws NotImplementedException {
        //Compiler compiler = new Compiler("C:\\Users\\Oliver\\Desktop\\JavaPP\\TestCode\\test.jpp");
        //compiler.simulate();
        if(args.length != 2){
            Logger.logger.log(Main.class, LogLevel.ERROR, "Invalid argument length");
            printUsage();
            return;
        }

        String path = args[0];
        String mode = args[1];

        Compiler compiler = new Compiler(path);

        if(mode.equalsIgnoreCase("sim")){
            compiler.simulate();
        } else if (mode.equalsIgnoreCase("com")){
            compiler.compile();
        } else {
            Logger.logger.log(Main.class, LogLevel.ERROR, "Invalid mode provided");
            printUsage();
            return;
        }

    }

    public static void printUsage(){
        System.out.println("---------- Usage ----------");
        System.out.println(" <com/sim>\t Choose if you want to compile or simulate the program");
        System.out.println(" <path>\t Path to source file");
        System.out.println("---------------------------");
    }
}
