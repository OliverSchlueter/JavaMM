package de.oliver.javapp.compiler;

import de.oliver.javapp.compiler.parser.*;
import de.oliver.javapp.compiler.parser.instructions.*;
import de.oliver.javapp.exceptions.*;
import de.oliver.javapp.utils.KeyValue;
import de.oliver.javapp.utils.Node;
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
                    && tokens.size() >= 4){

                // TODO: type checking
                Token type = tokens.get(0).getValue();
                Word identifier = tokens.get(1).getKey();

                Object value = null;

                LinkedList<KeyValue<Word, Token>> subWordTokens = new LinkedList<>();
                for (int i = 3; i < tokens.size(); i++) {
                    subWordTokens.add(tokens.get(i));
                }

                if(type == Token.TYPE_STRING){
                    value = combineStrings(program, subWordTokens);
                } else {
                    Node<KeyValue<Word, Token>> ast = astOfCalculation(subWordTokens);
                    //ast.print("");
                    value = calculateAst(program, ast);
                }
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
                    && tokens.get(1).getValue() == Token.EQUALS){

                String varName = tokens.get(0).getKey().value();
                Token dataType = program.getVariable(varName).getType();

                Object value = null;

                LinkedList<KeyValue<Word, Token>> subWordTokens = new LinkedList<>();
                for (int i = 2; i < tokens.size(); i++) {
                    subWordTokens.add(tokens.get(i));
                }

                if(dataType == Token.TYPE_STRING){
                    value = combineStrings(program, subWordTokens);
                } else {
                    Node<KeyValue<Word, Token>> ast = astOfCalculation(subWordTokens);
                    //ast.print("");
                    value = calculateAst(program, ast);
                }

                instruction = new AssignVariableInstruction(program, line, varName, value, dataType);
                instruction.execute();
                instruction = null;
            }

            program.addInstruction(instruction);
        }

        return program;
    }

    public Node<KeyValue<Word, Token>> astOfCalculation(LinkedList<KeyValue<Word, Token>> wordTokens){
        Node<KeyValue<Word, Token>> root = null;

        if(wordTokens.size() == 1){
            root = new Node<>(wordTokens.get(0));
            return root;
        }

        // TODO: only support for numbers at the moment
        // TODO: do type checking

        if(wordTokens.size() == 3){
            if(Token.arithmeticOperators().contains(wordTokens.get(1).getValue())){
                KeyValue<Word, Token> left = wordTokens.get(0);
                KeyValue<Word, Token> operator = wordTokens.get(1);
                KeyValue<Word, Token> right = wordTokens.get(2);

                if(!Token.arithmeticOperators().contains(operator.getValue())){
                    // TODO: invalid operator
                    return null;
                }

                root = new Node<>(operator, Arrays.asList(
                        new Node<>(left),
                        new Node<>(right)));
            return root;
            }
        }


        for (int i = 0; i < wordTokens.size(); i++) {
            Word word = wordTokens.get(i).getKey();
            Token token = wordTokens.get(i).getValue();

            // is operator
            if(Token.arithmeticOperators().contains(token)){

                if(root == null){
                    root = new Node<>(wordTokens.get(i))
                            .addChild(new Node<>(wordTokens.get(i-1)))
                            .addChild(new Node<>(wordTokens.get(i+1)));
                    continue;
                }

                if(token == Token.STAR || token == Token.SLASH){
                    KeyValue<Word, Token> left = wordTokens.get(i-1);
                    if(wordTokens.size() < i+1){
                        throw new NullPointerException("No value after operator at: " + word.formattedPosition());
                    }
                    KeyValue<Word, Token> right = wordTokens.get(i+1);

                    Node<KeyValue<Word, Token>> newChild = new Node<>(wordTokens.get(i))
                                                        .addChild(new Node<>(left))
                                                        .addChild(new Node<>(right));


                    Node<KeyValue<Word, Token>> lastRight = root.getLastRight();

                    if(lastRight == null){
                        lastRight = root;
                    }

                    lastRight.getChildren().remove(1);
                    lastRight.addChild(newChild);
                } else {
                    Node<KeyValue<Word, Token>> current = root.clone();
                    root = new Node<>(wordTokens.get(i));
                    root.addChild(new Node<>(wordTokens.get(i+1)));
                    root.addChild(current);
                }
            }

        }

        return root;
    }

    public double calculateAst(Program program, Node<KeyValue<Word, Token>> ast){

        Node<KeyValue<Word, Token>> lastParent = ast;

        while(lastParent.getChildren().size() == 2 && lastParent.getChildren().get(1).getChildren().size() == 2 ){
            lastParent = lastParent.getChildren().get(1);
        }

        if(lastParent.getParent() == null){
            if(lastParent.getChildren().size() == 2) {
                return calcOfOperatorNode(program, lastParent);
            } else if(Token.literals().contains(lastParent.getData().getValue())) {
                return Double.parseDouble(lastParent.getData().getKey().value());
            } else {
                return Double.MIN_VALUE;
            }
        } else {
            lastParent.setData(new KeyValue<>(new Word(-1, -1, calcOfOperatorNode(program, lastParent) + ""), lastParent.getChildren().get(0).getData().getValue()));
            lastParent.getChildren().clear();
            return calculateAst(program, ast);
        }
    }

    // TODO: currently only supports + - * /
    public double calcOfOperatorNode(Program program, Node<KeyValue<Word, Token>> node){
        double out = 0;
        KeyValue<Word, Token> leftChild = node.getChildren().get(0).getData();
        KeyValue<Word, Token> rightChild = node.getChildren().get(1).getData();

        double leftD = Token.literals().contains(leftChild.getValue()) ? Double.parseDouble(leftChild.getKey().value()) : (double) program.getVariable(leftChild.getKey().value()).getValue();
        double rightD = Token.literals().contains(rightChild.getValue()) ? Double.parseDouble(rightChild.getKey().value()) : (double) program.getVariable(rightChild.getKey().value()).getValue();

        switch (node.getData().getValue()){
            case PLUS -> out += leftD + rightD;
            case MINUS -> out += leftD - rightD;
            case STAR -> out += leftD * rightD;
            case SLASH -> out += leftD / rightD;
        }

        return out;
    }

    public String combineStrings(Program program, LinkedList<KeyValue<Word, Token>> wordTokens) throws InvalidTypeException, VariableNotFoundException {
        String s = "";
        for (KeyValue<Word, Token> wordToken : wordTokens) {
            String toAdd = "";

            if(wordToken.getValue() == Token.LITERAL_STRING){
                toAdd = wordToken.getKey().value();
                toAdd = toAdd.substring(1, toAdd.length() - 1);
            } else if(wordToken.getValue() == Token.IDENTIFIER){
                Variable var = program.getVariable(wordToken.getKey().value());
                if(var == null){
                    throw new VariableNotFoundException(wordToken.getKey().value());
                }

                if(var.getType() != Token.TYPE_STRING){
                    throw new InvalidTypeException(var, wordToken.getKey().line(), Token.TYPE_STRING);
                }

                toAdd = (String) var.getValue();
            } else if(wordToken.getValue() == Token.PLUS) {
                continue;
            } else {
                throw new InvalidTypeException(new Variable(null, wordToken.getValue(), wordToken.getKey().value()), wordToken.getKey().line(), Token.TYPE_STRING);
            }

            s += toAdd;
        }
        return s;
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
