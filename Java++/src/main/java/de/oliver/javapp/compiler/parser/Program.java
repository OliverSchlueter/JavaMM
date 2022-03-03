package de.oliver.javapp.compiler.parser;


import de.oliver.javapp.exceptions.*;
import de.oliver.javapp.utils.Token;
import de.oliver.logger.LogLevel;
import de.oliver.logger.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Program {

    private final LinkedList<Instruction> instructions;
    private final HashMap<String, Variable> variables;
    private final HashMap<String, Function> functions; // TODO: add support for same name but different attributes

    public Program(LinkedList<Instruction> instructions, HashMap<String, Variable> variables, HashMap<String, Function> functions) {
        this.instructions = instructions;
        this.variables = variables;
        this.functions = functions;
    }

    public Program(){
        this.instructions = new LinkedList<>();
        this.variables = new HashMap<>();
        this.functions = new HashMap<>();
    }

    /**
     * run the entire program
     */
    public void runProgram() throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {
        Logger.logger.log(Program.class, LogLevel.INFO, "Running program now");
        for (Instruction instruction : instructions) {
            instruction.execute();
        }
        Logger.logger.log(Program.class, LogLevel.INFO, "Finished program now");
    }

    /**
     * @return all instructions
     */
    public LinkedList<Instruction> getInstructions() {
        return instructions;
    }

    /**
     * adds an instruction to the program
     */
    public void addInstruction(Instruction instruction){
        if(instruction != null) {
            instructions.add(instruction);
        }
    }

    /**
     * @return all variables
     */
    public HashMap<String, Variable> getVariables() {
        return variables;
    }

    /**
     * @param name the name of the variable
     * @return if found it returns the variable, null if not
     */
    public Variable getVariable(String name){
        if(variables.containsKey(name)){
            return variables.get(name);
        } else {
            return null;
        }
    }

    /**
     * adds a variable to the program
     */
    public void addVariable(Variable variable){
        variables.put(variable.getName(), variable);
    }

    public void dumpVariables(){
        System.out.println("--------------------------");
        System.out.println("| Variables in program");
        System.out.println("| ");
        System.out.println("| <TYPE> <NAME> = <VALUE>");
        System.out.println("| ");
        for (Variable variable : variables.values()) {
            System.out.println("| " + variable.getType() + " " + variable.getName() + " = " + variable.getValue().toString());
        }
        System.out.println("--------------------------");
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
            for (Map.Entry<String, Token> entry : function.getAttributes().entrySet()) {
                attr += entry.getValue() + " " + entry.getKey() + ", ";
            }
            attr = attr.substring(0, attr.length()-2);
            System.out.println("| void " + name + " (" + attr + ")"); // TODO: add return value if implemented
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
