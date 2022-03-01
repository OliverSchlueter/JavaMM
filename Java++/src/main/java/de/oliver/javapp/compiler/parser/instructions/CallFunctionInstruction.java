package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.parser.*;
import de.oliver.javapp.exceptions.InvalidArgumentLengthException;
import de.oliver.javapp.exceptions.VariableNotFoundException;

import java.util.HashMap;
import java.util.List;

public class CallFunctionInstruction extends Instruction implements ParameterInstruction {

    private final String functionName;
    private final List<Variable> parametersVars;

    private Function function;

    public CallFunctionInstruction(Program program, int line, String functionName, List<Variable> parametersVars) {
        super(program, line);
        this.functionName = functionName;
        this.parametersVars = parametersVars;
        function = null;
    }

    @Override
    public void execute() throws InvalidArgumentLengthException, VariableNotFoundException {
        this.function = program.getFunction(functionName);

        HashMap<String, Variable> params = new HashMap<>();

        int attributeSize = function.getAttributes().keySet().size();
        int parameterSize = parametersVars.size();

        if (parameterSize != attributeSize) {
            throw new InvalidArgumentLengthException(attributeSize, parameterSize);
        }

        for (int i = 0; i < attributeSize; i++) {
            params.put(function.getAttributes().keySet().stream().toList().get(i), parametersVars.get(i));
        }

        execute(params);
    }

    @Override
    public void execute(HashMap<String, Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException {
        function.run(parameters);
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Variable> getParametersVars() {
        return parametersVars;
    }
}
