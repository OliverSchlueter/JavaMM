package de.oliver.javapp.compiler.parser;

import de.oliver.javapp.compiler.Parser;
import de.oliver.javapp.compiler.parser.instructions.ReturnInstruction;
import de.oliver.javapp.exceptions.*;
import de.oliver.javapp.utils.Token;

import java.util.*;

public class Function extends Block {

    //TODO: add return value for function

    private final String name;
    private final Token returnType;
    private Object returnValue;
    private final HashMap<String, Token> attributes;


    public Function(Program program, String name, Token returnType, HashMap<String, Variable> variables, HashMap<String, Token> attributes, LinkedList<Instruction> instructions) {
        super(program, instructions, variables);
        this.name = name;
        this.returnType = returnType;
        this.returnValue = null;
        this.attributes = attributes;
    }

    public Function(Program program, String name, Token returnType, HashMap<String, Token> attributes, LinkedList<Instruction> instructions) {
        super(program, instructions, new HashMap<>());
        this.name = name;
        this.returnType = returnType;
        this.returnValue = null;
        this.attributes = attributes;

        addDefaultVars();
    }

    public Function(Program program, String name, Token returnType, HashMap<String, Token> attributes){
        super(program, new LinkedList<>(), new HashMap<>());
        this.name = name;
        this.returnType = returnType;
        this.returnValue = null;
        this.attributes = attributes;

        addDefaultVars();
    }

    private void addDefaultVars(){
        for (Map.Entry<String, Token> entry : attributes.entrySet()) {
            addVariable(new Variable(entry.getKey(), entry.getValue(), '\0'));
        }

    }

    /**
     * Runs the function and sets the Function#getReturnValue() to the returned value
     * @param parameters key: attribute name
     *                   value: variable
     */
    public void run(List<Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {
        returnValue = null;

        int attrIndex = 0;
        for (Map.Entry<String, Token> entry : attributes.entrySet()) {
            String attrName = entry.getKey();
            Token attrType = entry.getValue();

            Variable var = variables.get(attrName);
            Variable p = parameters.get(attrIndex);
            // TODO: check if p var type = attrType
            var.setValue(p.getValue());

            attrIndex++;
        }

        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);

            if(instruction instanceof ReturnInstruction returnInstruction){
                if(returnValue == null){
                    if(returnType == Token.VOID && returnInstruction.getAst() == null){
                        break;
                    }
                    returnValue = Parser.calculateAst(returnInstruction.getBlock(), returnInstruction.getAst());
                    break;
                } else {
                    // TODO: return value is already set somehow
                }
            }

            if(instruction instanceof ParameterInstruction pInstr){
                pInstr.execute(parameters);
            } else {
                instruction.execute();
            }
        }

        if(returnType != Token.VOID && returnValue == null){
            // TODO: no return for function
        }
    }

    public String getName() {
        return name;
    }

    public Token getReturnType() {
        return returnType;
    }

    public HashMap<String, Token> getAttributes() {
        return attributes;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }
}
