package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.parser.Instruction;
import de.oliver.javapp.compiler.parser.ParameterInstruction;
import de.oliver.javapp.compiler.parser.Program;
import de.oliver.javapp.compiler.parser.Variable;
import de.oliver.javapp.exceptions.*;

import java.util.HashMap;

public class DumpInstruction extends Instruction implements ParameterInstruction {


    public DumpInstruction(Program program, int line) {
        super(program, line);
    }

    @Override
    public void execute() throws VariableNotFoundException, InvalidArgumentLengthException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {

    }

    @Override
    public void execute(HashMap<String, Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {
        Variable typeVar = parameters.get("type");
        String type = (String) typeVar.getValue();
        type = type.substring(1, type.length()-1);

        if(type.equalsIgnoreCase("variables")){
            program.dumpVariables();
        } else if (type.equalsIgnoreCase("functions")){
            program.dumpFunctions();
        } else {
            System.out.println("Invalid input for dump function: '" + type + "'");
        }
    }
}
