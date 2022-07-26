package de.oliver.javamm.compiler.parser;

import de.oliver.javamm.compiler.parser.instructions.DeclareBookmarkInstruction;
import de.oliver.javamm.exceptions.*;
import de.oliver.javamm.utils.Token;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class Block {

    protected final Block parentBlock;
    protected final LinkedList<Instruction> instructions;
    protected final HashMap<String, Variable> variables;
    protected final HashMap<String, Integer> bookmarks; // name of bookmark, index of instruction

    public Block(Block parentBlock, LinkedList<Instruction> instructions, HashMap<String, Variable> variables, HashMap<String, Integer> bookmarks) {
        this.parentBlock = parentBlock;
        this.instructions = instructions;
        this.variables = variables;
        this.bookmarks = bookmarks;
    }

    public abstract void run(List<Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException, NoReturnException, BookmarkAlreadyExistsException, NotImplementedException;

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

    public HashMap<String, Integer> getBookmarks() {
        return bookmarks;
    }

    public void addBookmark(String name, int line) throws BookmarkAlreadyExistsException {
        if(bookmarks.containsKey(name)){
            throw new BookmarkAlreadyExistsException(name);
        }

        bookmarks.put(name, line);
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

    public void dumpBookmarks(){
        String className = this.getClass().getSimpleName();
        System.out.println("--------------------------");
        System.out.println("| Bookmarks in " + className + (className.equals("Function") ? ": '" + ((Function) this).getName() + "'" : ""));
        System.out.println("| ");
        System.out.println("| <NAME> : <LINE>");
        System.out.println("| ");
        for (String bookmark : bookmarks.keySet()) {
            Instruction instr = instructions.get(bookmarks.get(bookmark) - 1);
            System.out.println("| " + bookmark + " : line " + instr.getLine());
        }
        System.out.println("--------------------------");
    }

}