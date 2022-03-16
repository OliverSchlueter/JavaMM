package de.oliver.javapp.compiler.parser;

import de.oliver.javapp.exceptions.*;
import de.oliver.javapp.utils.Token;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class Block {

    protected final Block parentBlock;
    protected final LinkedList<Instruction> instructions;
    protected final HashMap<String, Variable> variables;

    public Block(Block parentBlock, LinkedList<Instruction> instructions, HashMap<String, Variable> variables) {
        this.parentBlock = parentBlock;
        this.instructions = instructions;
        this.variables = variables;
    }

    public abstract void run(List<Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException;

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
        }
        return null;
    }

    public Block getParentBlock() {
        return parentBlock;
    }

    public List<Block> getAllParentBlocks(){
        List<Block> blocks = new LinkedList<>();
        if(parentBlock == this){
            blocks.add(this);
            return blocks;
        }

        if(parentBlock == null){
            blocks.add(this);
            return blocks;
        }

        Block current = parentBlock;
        do{
            blocks.add(current);
            current = current.getParentBlock();
        } while(current != null && current.getParentBlock() != null && !current.getParentBlock().equals(current));

        return blocks;
    }

    /**
     * adds a variable to the program
     */
    public void addVariable(Variable variable){
        variables.put(variable.getName(), variable);
    }

    public void addDeclaredVariable(String name, Token type){
        //declaredVariables.put(name, type);
        variables.put(name, new Variable(name, type, null));
    }

    public void dumpVariables(){
        String className = this.getClass().getSimpleName();
        System.out.println("--------------------------");
        System.out.println("| Variables in " + className + (className.equals("Function") ? ": '" + ((Function) this).getName() + "'" : ""));
        System.out.println("| ");
        System.out.println("| <TYPE> <NAME> = <VALUE>");
        System.out.println("| ");
        for (Variable variable : variables.values()) {
            Object val = variable.getValue();
            System.out.println("| " + variable.getType() + " " + variable.getName() + " = " + (val == null ? "null" : val));
        }
        System.out.println("--------------------------");
    }

}