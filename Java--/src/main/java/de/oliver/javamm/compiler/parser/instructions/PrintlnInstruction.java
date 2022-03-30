package de.oliver.javamm.compiler.parser.instructions;

import de.oliver.javamm.compiler.parser.*;

import java.util.List;

public class PrintlnInstruction extends Instruction implements ParameterInstruction {

    public PrintlnInstruction(Block block, int line) {
        super(block, block, line);
    }

    @Override
    public void execute() {

    }

    @Override
    public void execute(List<Variable> parameters) {
        Variable messageVar = parameters.get(0);
        if(messageVar.getValue() == null){
            System.out.println("null");
            return;
        }
        String message = messageVar.getValue().toString().startsWith("\"") && messageVar.getValue().toString().endsWith("\"")
                ? messageVar.getValue().toString().substring(1, messageVar.getValue().toString().length() - 1)
                : messageVar.getValue().toString();
        System.out.println(message);
    }
}
