package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.parser.*;
import de.oliver.javapp.exceptions.*;
import java.util.List;

public class CallFunctionInstruction extends Instruction {

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
    public void execute() throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {
        this.function = program.getFunction(functionName);

        if(this.function == null){
            throw new FunctionNotFoundException(functionName);
        }

        int attributeSize = function.getAttributes().keySet().size();
        int parameterSize = parametersVars.size();

        if (parameterSize != attributeSize) {
            throw new InvalidArgumentLengthException(attributeSize, parameterSize);
        }

        function.run(parametersVars);
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Variable> getParametersVars() {
        return parametersVars;
    }
}
