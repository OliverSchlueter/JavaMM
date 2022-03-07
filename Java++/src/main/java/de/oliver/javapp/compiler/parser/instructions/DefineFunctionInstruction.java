package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.parser.*;
import de.oliver.javapp.exceptions.*;
import de.oliver.javapp.utils.Token;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DefineFunctionInstruction extends DefineBlockInstruction {

    private final String name;
    private final Token returnType;
    private final HashMap<String, Token> attributes; // attribute name, type

    public DefineFunctionInstruction(Program program, int line, LinkedList<Instruction> instructions, String name, Token returnType, HashMap<String, Token> attributes) {
        super(program, line, instructions);
        this.name = name;
        this.returnType = returnType;
        this.attributes = attributes;
    }

    @Override
    public void execute() throws VariableNotFoundException, InvalidArgumentLengthException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {
        Function function = new Function(name, returnType, attributes, instructions);
        for (Map.Entry<String, Token> entry : attributes.entrySet()) {
            function.addVariable(new Variable(entry.getKey(), entry.getValue(), null));
        }
        program.addFunction(function);
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

}
