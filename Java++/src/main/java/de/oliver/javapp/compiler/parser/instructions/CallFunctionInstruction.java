package de.oliver.javapp.compiler.parser.instructions;

import de.oliver.javapp.compiler.parser.*;
import de.oliver.javapp.exceptions.*;
import java.util.List;

public class CallFunctionInstruction extends Instruction {

    private final String functionName;
    private final List<Variable> parametersVars;

    private Function function;

    public CallFunctionInstruction(Block parentBlock, Block block, int line, String functionName, List<Variable> parametersVars) {
        super(parentBlock, block, line);
        this.functionName = functionName;
        this.parametersVars = parametersVars;
        function = null;
    }

    @Override
    public void execute() throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException, NoReturnException {
        if(block instanceof Program pgr) {
            this.function = pgr.getFunction(functionName);
        } else {
            for (Block parentBlock : block.getAllParentBlocks()) {
                if (parentBlock instanceof Program pgr) {
                    this.function = pgr.getFunction(functionName);
                }
            }
        }

        if(this.function == null){
            throw new FunctionNotFoundException(functionName);
        }

        int attributeSize = function.getAttributes().keySet().size();
        int parameterSize = parametersVars.size();

        if (parameterSize != attributeSize) {
            throw new InvalidArgumentLengthException(attributeSize, parameterSize);
        }

        if(function.getName().equals("dump")){
            function.getInstructions().get(0).setBlock(block);
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
