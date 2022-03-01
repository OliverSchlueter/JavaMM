package de.oliver.javapp.compiler.parser;

import de.oliver.javapp.exceptions.InvalidArgumentLengthException;
import de.oliver.javapp.exceptions.VariableNotFoundException;
import de.oliver.javapp.utils.Token;

import java.util.HashMap;
import java.util.LinkedList;

public class Function {

    private final String name;
    private final HashMap<String, Token> attributes; // attribute name, type
    private final LinkedList<Instruction> instructions;

    public Function(String name, HashMap<String, Token> attributes, LinkedList<Instruction> instructions) {
        this.name = name;
        this.attributes = attributes;
        this.instructions = instructions;
    }

    /**
     * @param parameters key: attribute name
     *                   value: variable
     */
    public void run(HashMap<String, Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException {
        for (Instruction instruction : instructions) {
            if(instruction instanceof ParameterInstruction pInstr){
                pInstr.execute(parameters);
            } else {
                instruction.execute();
            }
        }
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Token> getAttributes() {
        return attributes;
    }

    public LinkedList<Instruction> getInstructions() {
        return instructions;
    }
}
