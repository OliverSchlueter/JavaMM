package de.oliver.javamm.compiler.parser;

import de.oliver.javamm.exceptions.*;
import de.oliver.javamm.utils.Token;
import de.oliver.logger.LogLevel;
import de.oliver.logger.Logger;

import java.util.*;

public class Program extends Block{

    private final HashMap<String, Function> functions; // TODO: add support for same name but different attributes

    public Program(LinkedList<Instruction> instructions, HashMap<String, Variable> variables, HashMap<String, Function> functions) {
        super(null, instructions, variables);
        this.functions = functions;
    }

    public Program(){
        super(null, new LinkedList<>(), new HashMap<>());
        this.functions = new HashMap<>();
    }

    /**
     * run the entire program
     */
    public void run(List<Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException, NoReturnException {
        Logger.logger.log(Program.class, LogLevel.INFO, "Running program now");
        for (int i = 0; i < instructions.size(); i++) {
            instructions.get(i).execute();
        }
        Logger.logger.log(Program.class, LogLevel.INFO, "Finished program now");
    }

    public void dumpFunctions(){
        System.out.println("--------------------------");
        System.out.println("| Functions in program");
        System.out.println("| ");
        System.out.println("| <RETURN TYPE> <NAME> <ATTRIBUTES>");
        System.out.println("| ");
        for (Function function : functions.values()) {
            String name = function.getName();
            String attr = "";
            if(function.getAttributes().size() > 0) {
                for (Map.Entry<String, Token> entry : function.getAttributes().entrySet()) {
                    attr += entry.getValue() + " " + entry.getKey() + ", ";
                }
                attr = attr.substring(0, attr.length() - 2);
            }
            System.out.println("| " + function.getReturnType() + " " + name + " (" + attr + ")");
        }
        System.out.println("--------------------------");
    }

    public HashMap<String, Function> getFunctions() {
        return functions;
    }

    /**
     * adds a function to the program
     */
    public void addFunction(Function function){
        functions.put(function.getName(), function);
    }

    /**
     * @param name the name of the function
     * @return if found it returns the function, null if not
     */
    public Function getFunction(String name){
        if(functions.containsKey(name)){
            return functions.get(name);
        } else {
            return null;
        }
    }
}
