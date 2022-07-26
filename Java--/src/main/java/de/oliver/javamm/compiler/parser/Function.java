package de.oliver.javamm.compiler.parser;

import de.oliver.javamm.compiler.Parser;
import de.oliver.javamm.compiler.parser.instructions.CallFunctionInstruction;
import de.oliver.javamm.compiler.parser.instructions.ReturnInstruction;
import de.oliver.javamm.exceptions.*;
import de.oliver.javamm.utils.Token;

import java.util.*;

public class Function extends Block {

    private final String name;
    private final Token returnType;
    private Object returnValue;
    private final HashMap<String, Token> attributes;


    public Function(Program program, String name, Token returnType, HashMap<String, Variable> variables, HashMap<String, Token> attributes, LinkedList<Instruction> instructions, HashMap<String, Integer> bookmarks) {
        super(program, instructions, variables, bookmarks);
        this.name = name;
        this.returnType = returnType;
        this.returnValue = null;
        this.attributes = attributes;
    }

    public Function(Program program, String name, Token returnType, HashMap<String, Token> attributes, LinkedList<Instruction> instructions) {
        super(program, instructions, new HashMap<>(), new HashMap<>());
        this.name = name;
        this.returnType = returnType;
        this.returnValue = null;
        this.attributes = attributes;

        addDefaultVars();
    }

    public Function(Program program, String name, Token returnType, HashMap<String, Token> attributes){
        super(program, new LinkedList<>(), new HashMap<>(), new HashMap<>());
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
    public void run(List<Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException, NoReturnException, BookmarkAlreadyExistsException {
        returnValue = null;

        int attrIndex = 0;
        for (Map.Entry<String, Token> entry : attributes.entrySet()) {
            String attrName = entry.getKey();
            Token attrType = entry.getValue();

            Variable var = variables.get(attrName);
            Variable p = parameters.get(attrIndex);

            if(p.getType() != Token.TYPE_OBJECT && var.getType() != Token.TYPE_OBJECT && p.getType() != attrType){
                throw new InvalidTypeException(p, -1, var.getType());
            }
            var.setValue(p.getValue());

            attrIndex++;
        }

        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);

            if(instruction instanceof CallFunctionInstruction callFunctionInstruction){
                if(callFunctionInstruction.getFunctionName().equals("exit")){
                    callFunctionInstruction.execute();
                    break;
                }
            }

            if(instruction instanceof ReturnInstruction returnInstruction){
                if(returnValue == null){
                    if(returnType == Token.VOID && returnInstruction.getAst() == null){
                        break;
                    }
                    returnValue = Parser.calculateAst(returnInstruction.getBlock(), returnInstruction.getAst());
                    break;
                }
            }

            if(instruction instanceof ParameterInstruction pInstr){
                pInstr.execute(parameters);
            } else {
                instruction.execute();
            }
        }

        if(returnType != Token.VOID && returnValue == null){
            throw new NoReturnException(this);
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
