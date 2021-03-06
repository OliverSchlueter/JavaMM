package de.oliver.javamm.compiler;

import de.oliver.javamm.compiler.parser.*;
import de.oliver.javamm.compiler.parser.instructions.*;
import de.oliver.javamm.exceptions.*;
import de.oliver.javamm.utils.KeyValue;
import de.oliver.javamm.utils.Node;
import de.oliver.javamm.utils.Token;
import de.oliver.javamm.utils.Word;
import de.oliver.logger.LogLevel;
import de.oliver.logger.Logger;

import java.util.*;

public class Parser {

    private final Map<Integer, LinkedList<KeyValue<Word, Token>>> tokens;

    public Parser(Map<Integer, LinkedList<KeyValue<Word, Token>>> tokens) {
        this.tokens = tokens;
    }

    public Program generateProgram() throws VariableNotFoundException, InvalidArgumentLengthException, FunctionNotFoundException, VariableAlreadyExistsException, InvalidTypeException, InvalidOperatorException, NoReturnException, FunctionInFunctionException, BookmarkAlreadyExistsException {
        Program program = new Program();

        addDefaultFunctions(program);

        LinkedList<DefineBlockInstruction> openBlocks = new LinkedList<>();


        for (Map.Entry<Integer, LinkedList<KeyValue<Word, Token>>> lineTokens : tokens.entrySet()) {
            int line = lineTokens.getKey();
            LinkedList<KeyValue<Word, Token>> tokens = lineTokens.getValue();

            Instruction instruction = null;

            // End of block
            if(tokens.get(0).getValue() == Token.CLOSE_BRACES){
                if(openBlocks.size() > 0){
                    DefineBlockInstruction instr = openBlocks.get(0);
                    instr.execute();
                    openBlocks.remove();
                    continue;
                }
            }

            // Declare variable
            else if(Token.basicDataTypes().contains(tokens.get(0).getValue())
                    && tokens.get(1).getValue() == Token.IDENTIFIER
                    && tokens.get(2).getValue() == Token.EQUALS
                    && tokens.size() >= 4){

                Token type = tokens.get(0).getValue();
                Word identifier = tokens.get(1).getKey();

                Object value = null;

                LinkedList<KeyValue<Word, Token>> subWordTokens = new LinkedList<>();
                for (int i = 3; i < tokens.size(); i++) {
                    subWordTokens.add(tokens.get(i));
                }

                Node<KeyValue<Word, Token>> ast;

                if(type == Token.TYPE_STRING){
                    ast = stringAst(subWordTokens);
                } else {
                    ast = astOfCalculation(subWordTokens);
                }
                //ast.print("");

                instruction = new DeclareVariableInstruction(program, program, line, identifier, type, ast);
                if(openBlocks.size() == 0) {
                    program.addDeclaredVariable(identifier.value(), type);
                } else {
                    if(openBlocks.get(0) instanceof DefineFunctionInstruction instr){
                        instruction.setBlock(instr.getFunction());
                        instr.getFunction().addDeclaredVariable(identifier.value(), type);
                    }
                }
            }

            // Assign variable
            else if(tokens.get(0).getValue() == Token.IDENTIFIER
                    && tokens.get(1).getValue() == Token.EQUALS){

                String varName = tokens.get(0).getKey().value();
                Token dataType = null;
                if(openBlocks.size() > 0){
                    if(openBlocks.get(0) instanceof DefineFunctionInstruction instr){
                        for (Block parent : instr.getFunction().getAllParentBlocks()) {
                            Variable var = parent.getVariable(varName);
                            if(var != null){
                                dataType = var.getType();
                                break;
                            }
                        }
                        if(instr.getFunction().getVariable(varName) != null){
                            dataType = instr.getFunction().getVariable(varName).getType();
                        }
                    }
                } else {
                    Variable var = program.getVariable(varName);
                    if(var != null){
                        dataType = var.getType();
                    }

                }

                if(dataType == null){
                    throw new VariableNotFoundException(varName);
                }

                LinkedList<KeyValue<Word, Token>> subWordTokens = new LinkedList<>();
                for (int i = 2; i < tokens.size(); i++) {
                    subWordTokens.add(tokens.get(i));
                }

                Node<KeyValue<Word, Token>> ast;

                if(dataType == Token.TYPE_STRING){
                    ast = stringAst(subWordTokens);
                    //ast.print("");
                } else {
                    ast = astOfCalculation(subWordTokens);
                }
                //ast.print("");

                instruction = new AssignVariableInstruction(program, program, line, varName, ast, dataType);
                if(openBlocks.size() > 0) {
                    if(openBlocks.get(0) instanceof DefineFunctionInstruction instr){
                        instruction.setBlock(instr.getFunction());
                    }
                }

            }

            // ++, --
            else if(tokens.get(0).getValue() == Token.IDENTIFIER
                    && (tokens.get(1).getValue() == Token.PLUS_PLUS || tokens.get(1).getValue() == Token.MINUS_MINUS)){
                String varName = tokens.get(0).getKey().value();

                Token dataType = null;
                if(openBlocks.size() > 0){
                    if(openBlocks.get(0) instanceof DefineFunctionInstruction instr){
                        for (Block parent : instr.getFunction().getAllParentBlocks()) {
                            Variable var = parent.getVariable(varName);
                            if(var != null){
                                dataType = var.getType();
                                break;
                            }
                        }
                        if(instr.getFunction().getVariable(varName) != null){
                            dataType = instr.getFunction().getVariable(varName).getType();
                        }
                    }
                } else {
                    Variable var = program.getVariable(varName);
                    if(var != null){
                        dataType = var.getType();
                    }

                }

                if(dataType == null){
                    throw new VariableNotFoundException(varName);
                }

                if(tokens.get(1).getValue() == Token.MINUS_MINUS) {
                    instruction = new DecrementInstruction(program, program, line, varName, dataType);
                } else {
                    instruction = new IncrementInstruction(program, program, line, varName, dataType);
                }
                if(openBlocks.size() > 0) {
                    if(openBlocks.get(0) instanceof DefineFunctionInstruction instr){
                        instruction.setBlock(instr.getFunction());
                    }
                }
            }

            // Call function
             else if(tokens.get(0).getValue() == Token.IDENTIFIER
                    && tokens.get(1).getValue() == Token.OPEN_PARENTHESIS
                    && tokens.get(tokens.size()-1).getValue() == Token.CLOSE_PARENTHESIS){

                String functionName = tokens.get(0).getKey().value();
                List<Variable> parameters = new ArrayList<>();

                for (int i = 2; i < tokens.size(); i++) {
                    Word word = tokens.get(i).getKey();
                    Token token = tokens.get(i).getValue();
                    // TODO: do calculating in parameter
                    // for now it just accepts just one variable or literal
                    if (token == Token.IDENTIFIER){
                        Variable var = null;
                        if(openBlocks.size() > 0){
                            if(openBlocks.get(0) instanceof DefineFunctionInstruction instr) {
                                var = instr.getFunction().getVariable(word.value());
                            }
                        }

                        if(var == null){
                            var = program.getVariable(word.value());
                        }

                        if(var == null){
                            Logger.logger.log(Parser.class, LogLevel.ERROR, "Variable not found: '" + word.value() + "' at " + word.formattedPosition());
                            throw new VariableNotFoundException(word.value());
                        }

                        parameters.add(var);
                    } else if(Token.literals().contains(token)){
                        Variable var = new Variable(null, Token.getTypeOfLiteral(token), word.value());
                        parameters.add(var);
                    }
                }

                instruction = new CallFunctionInstruction(program, program, line, functionName, parameters);

                if(openBlocks.size() > 0) {
                    if(openBlocks.get(0) instanceof DefineFunctionInstruction instr){
                        instruction.setBlock(instr.getFunction());
                    }
                }
            }

             // Define function
            else if(tokens.get(0).getValue() == Token.DEF
                    && Token.basicDataTypes().contains(tokens.get(1).getValue())
                    && tokens.get(2).getValue() == Token.IDENTIFIER
                    && tokens.get(3).getValue() == Token.OPEN_PARENTHESIS
                    && tokens.get(tokens.size() - 1).getValue() == Token.OPEN_BRACES
                    && tokens.get(tokens.size() - 2).getValue() == Token.CLOSE_PARENTHESIS){

                Token returnType = tokens.get(1).getValue();
                String identifier = tokens.get(2).getKey().value();

                HashMap<String, Token> attributes = new HashMap<>(); // attribute name, type

                for (int i = 4; i < tokens.size() - 2; i+=1) {
                    Token token = tokens.get(i).getValue();

                    if(Token.basicDataTypes().contains(token)){
                        if(tokens.get(i+1).getValue() == Token.IDENTIFIER){
                            attributes.put(tokens.get(i + 1).getKey().value(), token);
                        }
                    }

                }

                for (DefineBlockInstruction openBlock : openBlocks) {
                    if(openBlock instanceof DefineFunctionInstruction){
                        throw new FunctionInFunctionException();
                    }
                }

                DefineFunctionInstruction defineFunctionInstruction = new DefineFunctionInstruction(program, program, line, new LinkedList<>(), identifier, returnType, attributes);
                openBlocks.add(defineFunctionInstruction);
                continue;
            }

            // Return
            else if (tokens.get(0).getValue() == Token.RETURN){
                LinkedList<KeyValue<Word, Token>> subWordTokens = new LinkedList<>();
                for (int i = 1; i < tokens.size(); i++) {
                    subWordTokens.add(tokens.get(i));
                }

                Node<KeyValue<Word, Token>> ast;

                ast = astOfCalculation(subWordTokens);

                instruction = new ReturnInstruction(program, program, line, ast);

                if(openBlocks.size() > 0) {
                    if(openBlocks.get(0) instanceof DefineFunctionInstruction instr){
                        instruction.setBlock(instr.getFunction());
                    }
                }
            }

            // Declare bookmark
            else if(tokens.get(0).getValue() == Token.POINT && tokens.get(1).getValue() == Token.IDENTIFIER){
                instruction = new DeclareBookmarkInstruction(program, program, line, tokens.get(1).getKey().value());
                instruction.execute();
                continue;
            }

            // GOTO
            else if(tokens.get(0).getValue() == Token.GOTO && tokens.get(1).getValue() == Token.IDENTIFIER){
                instruction = new GotoInstruction(program, program, line, tokens.get(1).getKey().value());
            }


            if(instruction == null){
                continue;
            }

            if(openBlocks.size() == 0) {
                program.addInstruction(instruction);
            } else {
                openBlocks.get(0).addInstruction(instruction);
            }
        }

        return program;
    }


    public Node<KeyValue<Word, Token>> astOfCalculation(LinkedList<KeyValue<Word, Token>> wordTokens) throws InvalidOperatorException {
        Node<KeyValue<Word, Token>> root = null;

        if(wordTokens.size() == 1){
            root = new Node<>(wordTokens.get(0));
            return root;
        }

        // TODO: only support for numbers at the moment

        if(wordTokens.size() == 3){
            if(Token.arithmeticOperators().contains(wordTokens.get(1).getValue())){
                KeyValue<Word, Token> left = wordTokens.get(0);
                KeyValue<Word, Token> operator = wordTokens.get(1);
                KeyValue<Word, Token> right = wordTokens.get(2);

                if(!Token.arithmeticOperators().contains(operator.getValue())){
                    throw new InvalidOperatorException(operator.getValue());
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

    public static double calculateAst(Block block, Node<KeyValue<Word, Token>> ast){

        Node<KeyValue<Word, Token>> lastParent = ast;

        while(lastParent.getChildren().size() == 2 && lastParent.getChildren().get(1).getChildren().size() == 2 ){
            lastParent = lastParent.getChildren().get(1);
        }

        if(lastParent.getParent() == null){
            if(lastParent.getChildren().size() == 2) {
                return calcOfOperatorNode(block, lastParent);
            } else if(Token.literals().contains(lastParent.getData().getValue())) {
                return Double.parseDouble(lastParent.getData().getKey().value());
            } else {
                return (double) block.getVariable(lastParent.getData().getKey().value()).getValue();
            }
        } else {
            lastParent.setData(new KeyValue<>(new Word(-1, -1, calcOfOperatorNode(block, lastParent) + ""), lastParent.getChildren().get(0).getData().getValue()));
            lastParent.getChildren().clear();
            return calculateAst(block, ast);
        }
    }

    // TODO: currently only supports + - * / %
    public static double calcOfOperatorNode(Block block, Node<KeyValue<Word, Token>> node){
        double out = 0;
        KeyValue<Word, Token> leftChild = node.getChildren().get(0).getData();
        KeyValue<Word, Token> rightChild = node.getChildren().get(1).getData();

            double leftD = Token.literals().contains(leftChild.getValue())
                    ? Double.parseDouble(leftChild.getKey().value())
                    : Double.parseDouble(block.getVariable(leftChild.getKey().value()).getValue().toString());

            double rightD = Token.literals().contains(rightChild.getValue())
                    ? Double.parseDouble(rightChild.getKey().value())
                    : Double.parseDouble(block.getVariable(rightChild.getKey().value()).getValue().toString());

        switch (node.getData().getValue()){
            case PLUS -> out += leftD + rightD;
            case MINUS -> out += leftD - rightD;
            case STAR -> out += leftD * rightD;
            case SLASH -> out += leftD / rightD;
            case PERCENTAGE -> out += leftD % rightD;
            case DOUBLE_STAR -> out += Math.pow(leftD, rightD);
            case DOUBLE_SLASH -> out += Math.floor(leftD / rightD);
        }

        return out;
    }

    public Node<KeyValue<Word, Token>> stringAst(LinkedList<KeyValue<Word, Token>> wordTokens){
        Node<KeyValue<Word, Token>> ast = new Node<>(new KeyValue<>(new Word(-1, -1, ""), Token.LITERAL_STRING));
        for (KeyValue<Word, Token> wordToken : wordTokens) {
            ast.addChild(new Node<>(wordToken));
        }

        return ast;
    }

    public static String calcStringAst(Block block, Node<KeyValue<Word, Token>> ast) throws VariableNotFoundException, InvalidTypeException {
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < ast.getChildren().size(); i++) {
            Token type = ast.getChildren().get(i).getData().getValue();
            String data = ast.getChildren().get(i).getData().getKey().value();
            if(type == Token.LITERAL_STRING){
                data = data.substring(1, data.length() - 1);
                out.append(data);
            } else if(type == Token.IDENTIFIER){
                Variable var = block.getVariable(data);
                if(var == null){
                    throw new VariableNotFoundException(data);
                }

                String val = var.getValue().toString();
                if(val.startsWith("\"") && val.endsWith("\"")) {
                    val = val.substring(1, val.length() - 1);
                }
                out.append(val);
            }

        }

        return out.toString();
    }

    @Deprecated
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

        Function printFunc = new Function(program,"print", Token.VOID, printAttr, printInstr);
        program.addFunction(printFunc);

        //PRINTLN
        HashMap<String, Token> printlnAttr = new HashMap<>();
        printlnAttr.put("message", Token.TYPE_OBJECT);

        LinkedList<Instruction> printlnInstr = new LinkedList<>();
        printlnInstr.add(new PrintlnInstruction(program, -1));

        Function printlnFunc = new Function(program, "println", Token.VOID, printlnAttr, printlnInstr);
        program.addFunction(printlnFunc);

        //DUMP
        HashMap<String, Token> dumpAttr = new HashMap<>();
        dumpAttr.put("type", Token.TYPE_STRING);

        LinkedList<Instruction> dumpInstr = new LinkedList<>();
        dumpInstr.add(new DumpInstruction(program, program, -1));

        Function dumpFunc = new Function(program, "dump", Token.VOID, dumpAttr, dumpInstr);
        program.addFunction(dumpFunc);

        // INPUT
        HashMap<String, Token> inputAttr = new HashMap<>();
        inputAttr.put("type", Token.TYPE_STRING);

        LinkedList<Instruction> inputInstr = new LinkedList<>();
        program.addDeclaredVariable("$USER_INPUT", Token.TYPE_STRING);
        inputInstr.add(new InputInstruction(program, program, -1, "$USER_INPUT"));
        // TODO: add return instr for user input
        // at the moment it sets the variable "$USER_INPUT" to the user input

        Function inputFunc = new Function(program, "input", Token.VOID, inputAttr, inputInstr);
        program.addFunction(inputFunc);

        //CLEAR CONSOLE
        HashMap<String, Token> clearConsoleAttr = new HashMap<>();

        LinkedList<Instruction> clearConsoleInstr = new LinkedList<>();
        clearConsoleInstr.add(new ClearConsoleInstruction(program, program, -1));
        Function clearConsoleFunc = new Function(program, "clearConsole", Token.VOID, clearConsoleAttr, clearConsoleInstr);
        program.addFunction(clearConsoleFunc);

        //EXIT
        LinkedList<Instruction> exitInstr = new LinkedList<>();
        exitInstr.add(new ExitInstruction(program, program, -1));
        Function exitFunc = new Function(program, "exit", Token.VOID, new HashMap<>(), exitInstr);
        program.addFunction(exitFunc);
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

    public static Token getTypeOfObject(Object obj){
        Token type = null;

        switch (obj.getClass().getSimpleName().toLowerCase(Locale.ROOT)){
            case "boolean" -> { type = Token.TYPE_BOOLEAN; }
            case "character" -> { type = Token.TYPE_CHARACTER; }
            case "string" -> { type = Token.TYPE_STRING; }
            case "integer" -> { type = Token.TYPE_INTEGER; }
            case "long" -> { type = Token.TYPE_LONG; }
            case "float" -> { type = Token.TYPE_FLOAT; }
            case "double" -> { type = Token.TYPE_DOUBLE; }
            default -> { type = Token.TYPE_OBJECT; }
        }

        return type;
    }

    public static Object castToType(Object obj, Token type){
        switch (type) {
            case TYPE_BOOLEAN -> { return Boolean.parseBoolean(obj.toString()); }
            case TYPE_STRING -> { return obj.toString(); }
            case TYPE_CHARACTER -> { return obj.toString().toCharArray()[0]; }
            case TYPE_INTEGER -> { return Integer.parseInt(obj.toString()); }
            case TYPE_LONG -> { return Long.parseLong(obj.toString()); }
            case TYPE_FLOAT -> { return Float.parseFloat(obj.toString()); }
            case TYPE_DOUBLE -> { return Double.parseDouble(obj.toString()); }
        }

        return obj;
    }
}
