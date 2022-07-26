package de.oliver.javamm.compiler.parser.instructions;

import de.oliver.javamm.compiler.parser.*;
import de.oliver.javamm.exceptions.*;

import java.util.List;

public class DumpInstruction extends Instruction implements ParameterInstruction {


    public DumpInstruction(Block parentBlock, Block block, int line) {
        super(parentBlock, block, line);
    }

    @Override
    public void execute() throws VariableNotFoundException, InvalidArgumentLengthException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {

    }

    @Override
    public void execute(List<Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {
        Variable typeVar = parameters.get(0);
        String type = (String) typeVar.getValue();
        type = type.substring(1, type.length()-1);

        if(type.equalsIgnoreCase("variables")){
            block.dumpVariables();
        } else if (type.equalsIgnoreCase("functions")){
            ((Program) block).dumpFunctions();
        } else if (type.equalsIgnoreCase("bookmarks")){
            ((Program) block).dumpBookmarks();
        }else {
            System.out.println("Invalid input for dump function: '" + type + "'");
        }
    }
}
