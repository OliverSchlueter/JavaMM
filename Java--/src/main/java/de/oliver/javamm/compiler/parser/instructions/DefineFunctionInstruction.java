package de.oliver.javamm.compiler.parser.instructions;

import de.oliver.javamm.compiler.parser.*;
import de.oliver.javamm.exceptions.*;
import de.oliver.javamm.utils.Token;

import java.util.HashMap;
import java.util.LinkedList;

public class DefineFunctionInstruction extends DefineBlockInstruction {

    private final String name;
    private final Token returnType;
    private final HashMap<String, Token> attributes; // attribute name, type

    private final Function function;

    public DefineFunctionInstruction(Program program, Block block, int line, LinkedList<Instruction> instructions, String name, Token returnType, HashMap<String, Token> attributes) {
        super(program, block, line, instructions);
        this.name = name;
        this.returnType = returnType;
        this.attributes = attributes;
        this.function = new Function(program, name, returnType, attributes);
    }

    @Override
    public void execute() throws VariableNotFoundException, InvalidArgumentLengthException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {
        //Function function = new Function((Program) this.block.getParentBlock(), name, returnType, attributes, instructions);
        function.getInstructions().addAll(instructions);

        ((Program) block).addFunction(function);
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

    public Function getFunction() {
        return function;
    }
}
