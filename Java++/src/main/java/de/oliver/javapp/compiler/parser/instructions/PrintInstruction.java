package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.parser.Instruction;
import de.oliver.javapp.compiler.parser.ParameterInstruction;
import de.oliver.javapp.compiler.parser.Program;
import de.oliver.javapp.compiler.parser.Variable;

import java.util.HashMap;

public class PrintInstruction extends Instruction implements ParameterInstruction {

    public PrintInstruction(Program program, int line) {
        super(program, line);
    }

    @Override
    public void execute() {

    }

    @Override
    public void execute(HashMap<String, Variable> parameters) {
        System.out.println(parameters.get("message").getValue().toString());
    }
}
