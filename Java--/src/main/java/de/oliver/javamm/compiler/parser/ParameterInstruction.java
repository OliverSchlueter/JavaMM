package de.oliver.javamm.compiler.parser;

import de.oliver.javamm.exceptions.*;

import java.util.List;

public interface ParameterInstruction {

    public void execute(List<Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException;

}
