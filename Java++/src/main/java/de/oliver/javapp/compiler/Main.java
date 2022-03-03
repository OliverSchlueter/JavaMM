package de.oliver.javapp.compiler;

public class Main {

    public static void main(String[] args) {
        Compiler compiler = new Compiler("D:\\Workspaces\\IntelliJ\\JavaPP\\TestCode\\test.jpp");
        compiler.simulate();
    }

    public void printUsage(){
        System.out.println("---------- Usage ----------");
        System.out.println(" -src <path>\t Path to source file");
        System.out.println(" -mod <com/sim> \t Choose if you want to compile or simulate the program");
        System.out.println("---------------------------");
    }
}
