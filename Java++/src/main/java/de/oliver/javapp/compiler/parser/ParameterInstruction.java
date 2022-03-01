package de.oliver.javapp.compiler.parser;

import de.oliver.javapp.exceptions.*;

import java.util.HashMap;

public interface ParameterInstruction {

    public void execute(HashMap<String, Variable> parameters) throws InvalidArgumentLengthException, VariableNotFoundException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException;

}
