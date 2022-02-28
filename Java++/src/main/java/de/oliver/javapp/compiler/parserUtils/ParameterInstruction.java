package de.oliver.javapp.compiler.parserUtils;

import java.util.HashMap;

public interface ParameterInstruction {

    public void execute(HashMap<String, Variable> parameters);

}
