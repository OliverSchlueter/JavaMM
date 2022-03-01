package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.parser.Instruction;
import de.oliver.javapp.compiler.parser.ParameterInstruction;
import de.oliver.javapp.compiler.parser.Program;
import de.oliver.javapp.compiler.parser.Variable;
import java.util.HashMap;

public class PrintlnInstruction extends Instruction implements ParameterInstruction {

    public PrintlnInstruction(Program program, int line) {
        super(program, line);
    }

    @Override
    public void execute() {

    }

    @Override
    public void execute(HashMap<String, Variable> parameters) {
        Variable messageVar = parameters.get("message");
        String message = messageVar.getValue().toString().startsWith("\"") && messageVar.getValue().toString().endsWith("\"")
                ? messageVar.getValue().toString().substring(1, messageVar.getValue().toString().length() - 1)
                : messageVar.getValue().toString();
        System.out.println(message);
    }
}
