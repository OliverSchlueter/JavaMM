package de.oliver.javapp.compiler;

import de.oliver.javapp.compiler.parser.*;
import de.oliver.javapp.compiler.parser.instructions.*;
import de.oliver.javapp.exceptions.*;
import de.oliver.javapp.utils.KeyValue;
import de.oliver.javapp.utils.Token;
import de.oliver.javapp.utils.Word;
import de.oliver.logger.LogLevel;
import de.oliver.logger.Logger;
import jdk.jshell.EvalException;

import java.util.*;

public class Parser {

    private final Map<Integer, LinkedList<KeyValue<Word, Token>>> tokens;

    public Parser(Map<Integer, LinkedList<KeyValue<Word, Token>>> tokens) {
        this.tokens = tokens;
    }

    public Program generateProgram() throws VariableNotFoundException, InvalidArgumentLengthException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException {
        Program program = new Program();

        addDefaultFunctions(program);

        for (Map.Entry<Integer, LinkedList<KeyValue<Word, Token>>> lineTokens : tokens.entrySet()) {
            int line = lineTokens.getKey();
            LinkedList<KeyValue<Word, Token>> tokens = lineTokens.getValue();

            Instruction instruction = null;

            // Declare variable
            if(Token.basicDataTypes().contains(tokens.get(0).getValue())
                    && tokens.get(1).getValue() == Token.IDENTIFIER
                    && tokens.get(2).getValue() == Token.EQUALS
                    && Token.literals().contains(tokens.get(3).getValue())){ // or is another var

                // TODO: add calculating here
                // TODO: type checking
                Token type = tokens.get(0).getValue();
                Word identifier = tokens.get(1).getKey();
                Object value = stringToType(tokens.get(3).getKey().value(), type);
                instruction = new DeclareVariableInstruction(program, line, identifier, type, value);
                instruction.execute();
                instruction = null;
            }

            // Call function
             else if(tokens.get(0).getValue() == Token.IDENTIFIER
                    && tokens.get(1).getValue() == Token.OPEN_PARENTHESIS
                    && tokens.get(tokens.size()-1).getValue() == Token.CLOSE_PARENTHESIS){

                //TODO: more more checks
                String functionName = tokens.get(0).getKey().value();
                List<Variable> parameters = new ArrayList<>();

                for (int i = 2; i < tokens.size(); i++) {
                    Word word = tokens.get(i).getKey();
                    Token token = tokens.get(i).getValue();
                    // TODO: do calculating if needed here
                    // for now it just accepts just one variable or literal
                    if (token == Token.IDENTIFIER){
                        Variable var = program.getVariable(word.value());
                        if(var == null){
                            Logger.logger.log(Parser.class, LogLevel.ERROR, "Variable not found: '" + word.value() + "' at " + word.formattedPosition());
                            throw new VariableNotFoundException(word.value());
                        }

                        parameters.add(var);
                    } else if(Token.literals().contains(token)){
                        Variable var = new Variable(null, Token.getTypeOfLiteral(token), word.value()); //TODO: find correct type for literal
                        parameters.add(var);
                    }
                }

                instruction = new CallFunctionInstruction(program, line, functionName, parameters);
            }

             // Assign variable
            else if(tokens.get(0).getValue() == Token.IDENTIFIER
                    && tokens.get(1).getValue() == Token.EQUALS
                    && Token.literals().contains(tokens.get(2).getValue())){ // TODO: do calculating if needed here

                String varName = tokens.get(0).getKey().value();
                String data = tokens.get(2).getKey().value();
                Token dataType = Token.getTypeOfLiteral(tokens.get(2).getValue());
                Object value = stringToType(data, dataType);

                instruction = new AssignVariableInstruction(program, line, varName, value, dataType);
            }

            program.addInstruction(instruction);
        }

        return program;
    }


    public void addDefaultFunctions(Program program){
        // PRINT
        HashMap<String, Token> printAttr = new HashMap<>();
        printAttr.put("message", Token.TYPE_OBJECT);

        LinkedList<Instruction> printInstr = new LinkedList<>();
        printInstr.add(new PrintInstruction(program, -1));

        Function printFunc = new Function("print", printAttr, printInstr);
        program.addFunction(printFunc);

        //PRINTLN
        HashMap<String, Token> printlnAttr = new HashMap<>();
        printlnAttr.put("message", Token.TYPE_OBJECT);

        LinkedList<Instruction> printlnInstr = new LinkedList<>();
        printlnInstr.add(new PrintlnInstruction(program, -1));

        Function printlnFunc = new Function("println", printlnAttr, printlnInstr);
        program.addFunction(printlnFunc);

        //DUMP
        HashMap<String, Token> dumpAttr = new HashMap<>();
        dumpAttr.put("type", Token.TYPE_STRING);

        LinkedList<Instruction> dumpInstr = new LinkedList<>();
        dumpInstr.add(new DumpInstruction(program, -1));

        Function dumpFunc = new Function("dump", dumpAttr, dumpInstr);
        program.addFunction(dumpFunc);
    }

    public Map<Integer, LinkedList<KeyValue<Word, Token>>> getTokens() {
        return tokens;
    }

    private static Object stringToType(String data, Token type){
        switch (type){
            case TYPE_STRING, LITERAL_STRING -> { return data.replace("\"", ""); }
            case TYPE_BOOLEAN, LITERAL_BOOLEAN -> { return Boolean.parseBoolean(data); }
            case TYPE_CHARACTER, LITERAL_CHARACTER -> { return data.toCharArray()[1]; }
            case TYPE_DOUBLE, LITERAL_DOUBLE -> { return Double.parseDouble(data); }
            case TYPE_FLOAT, LITERAL_FLOAT -> { return Float.parseFloat(data); }
            case TYPE_INTEGER, LITERAL_INTEGER -> { return Integer.parseInt(data); }
            case TYPE_LONG, LITERAL_LONG -> { return Long.parseLong(data); }
        }

        return null;
    }
}
