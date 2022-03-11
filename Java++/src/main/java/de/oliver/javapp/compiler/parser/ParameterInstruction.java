package de.oliver.javapp.compiler.parser;

import de.oliver.javapp.exceptions.*;

import java.util.List;

public interface ParameterInstruction {

    public void execute(List<Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException;

}
