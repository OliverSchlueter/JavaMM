package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.parser.*;
import de.oliver.javapp.exceptions.*;
import de.oliver.javapp.utils.KeyValue;
import de.oliver.javapp.utils.Token;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    public void execute() throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {
        this.function = program.getFunction(functionName);

        if(this.function == null){
            throw new FunctionNotFoundException(functionName);
        }

        HashMap<String, Variable> params = new HashMap<>();

        int attributeSize = function.getAttributes().keySet().size();
        int parameterSize = parametersVars.size();

        if (parameterSize != attributeSize) {
            throw new InvalidArgumentLengthException(attributeSize, parameterSize);
        }

        for (int i = 0; i < attributeSize; i++) {
            Variable p = parametersVars.get(i);
            KeyValue<String, Token> a = new KeyValue<>(function.getAttributes().entrySet().stream().toList().get(0).getKey(), function.getAttributes().entrySet().stream().toList().get(0).getValue());
            if(a.getValue() != Token.TYPE_OBJECT && p.getType() != a.getValue()){
                throw new InvalidTypeException(p, line, a.getValue());
            }
            params.put(function.getAttributes().keySet().stream().toList().get(i), p);
        }

        execute(params);
    }

    @Override
    public void execute(HashMap<String, Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {
        function.run(parameters);
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Variable> getParametersVars() {
        return parametersVars;
    }
}
