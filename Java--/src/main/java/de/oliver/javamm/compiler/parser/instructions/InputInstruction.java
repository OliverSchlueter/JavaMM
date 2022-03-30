package de.oliver.javamm.compiler.parser.instructions;

import de.oliver.javamm.compiler.parser.Block;
import de.oliver.javamm.compiler.parser.Instruction;
import de.oliver.javamm.compiler.parser.ParameterInstruction;
import de.oliver.javamm.compiler.parser.Variable;
import de.oliver.javamm.exceptions.*;
import de.oliver.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class InputInstruction extends Instruction implements ParameterInstruction {

    private String targetVariableName;
    private String userInput;

    public InputInstruction(Block parentBlock, Block block, int line, String targetVariableName) {
        super(parentBlock, block, line);
        this.targetVariableName = targetVariableName;
        this.userInput = null;
    }

    @Override
    public void execute() throws VariableNotFoundException, InvalidArgumentLengthException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException, NoReturnException {

    }

    @Override
    public void execute(List<Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {
        String str = parameters.get(0).getValue().toString();
        str = str.substring(1, str.length()-1);
        System.out.print(str);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            userInput = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Variable var = block.getVariable(targetVariableName);
        var.setValue(userInput);
    }

    public String getUserInput() {
        return userInput;
    }

    public String getTargetVariableName() {
        return targetVariableName;
    }
}
