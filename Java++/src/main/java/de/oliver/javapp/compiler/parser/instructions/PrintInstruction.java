package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.parser.*;

import java.util.HashMap;
import java.util.List;

public class PrintInstruction extends Instruction implements ParameterInstruction {

    public PrintInstruction(Block block, int line) {
        super(block, block, line);
    }

    @Override
    public void execute() {

    }

    @Override
    public void execute(List<Variable> parameters) {
        Variable messageVar = parameters.get(0);
        String message = messageVar.getValue().toString().startsWith("\"") && messageVar.getValue().toString().endsWith("\"")
                ? messageVar.getValue().toString().substring(1, messageVar.getValue().toString().length() - 1)
                : messageVar.getValue().toString();
        System.out.print(message);
    }
}
