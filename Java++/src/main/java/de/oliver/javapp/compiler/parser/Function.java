package de.oliver.javapp.compiler.parser;

import de.oliver.javapp.exceptions.*;
import de.oliver.javapp.utils.Token;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Function {

    //TODO: add return value for function

    private final String name;
    private final Token returnType;
    private final HashMap<String, Variable> variables;
    private final HashMap<String, Token> attributes; // attribute name, type
    private final LinkedList<Instruction> instructions;

    public Function(String name, Token returnType, HashMap<String, Token> attributes, LinkedList<Instruction> instructions) {
        this.name = name;
        this.returnType = returnType;
        this.attributes = attributes;
        this.instructions = instructions;
        this.variables = new HashMap<>();
    }

    /**
     * @param parameters key: attribute name
     *                   value: variable
     * @return return value of the function
     */
    public Object run(List<Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {
        Object returnValue = null;

        for (Instruction instruction : instructions) {
            if(instruction instanceof ParameterInstruction pInstr){
                pInstr.execute(parameters);
            } else {
                instruction.execute();
            }
        }

        if(returnType == Token.VOID){
            return null;
        }

        return returnValue; //TODO: add return, maybe with interface ReturnInstruction
    }

    public String getName() {
        return name;
    }

    public Token getReturnType() {
        return returnType;
    }

    public HashMap<String, Variable> getVariables() {
        return variables;
    }

    public Variable getVariable(String name){
        if(variables.containsKey(name)){
            return variables.get(name);
        } else {
            return null;
        }
    }

    public void addVariable(Variable variable){
        variables.put(variable.getName(), variable);
    }

    public HashMap<String, Token> getAttributes() {
        return attributes;
    }

    public LinkedList<Instruction> getInstructions() {
        return instructions;
    }
}
