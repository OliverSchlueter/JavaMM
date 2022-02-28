package de.oliver.javapp.compiler;

import de.oliver.javapp.utils.KeyValue;
import de.oliver.javapp.utils.Node;
import de.oliver.javapp.utils.Token;
import de.oliver.javapp.utils.Word;

import java.util.LinkedList;
import java.util.Map;

public class Parser {

    private final Map<Integer, LinkedList<KeyValue<Word, Token>>> tokens;

    private Node<Word> rootExpression;

    public Parser(Map<Integer, LinkedList<KeyValue<Word, Token>>> tokens) {
        this.tokens = tokens;
        this.rootExpression = new Node<>(null);
    }

    public Node<Word> parse(LinkedList<KeyValue<Word, Token>> tokens, Node<Word> current){

        // full plus
        if(tokens.size() >= 3 && tokens.get(1).getValue() == Token.PLUS){
            Node<Word> plusNode = new Node<>(tokens.get(1).getKey());
            plusNode.addChild(new Node<>(tokens.get(0).getKey()));
            plusNode.addChild(new Node<>(tokens.get(2).getKey()));

            if(current == null){
                current = plusNode;
            } else {
                current.addChild(plusNode);
            }

            tokens.removeFirst();
            tokens.removeFirst();
            tokens.removeFirst();

            current = parse(tokens, current);

        } else if(tokens.get(0).getValue() == Token.PLUS) {
            Node<Word> plusNode = new Node<>(tokens.get(0).getKey());
            tokens.removeFirst();
            current = parse(tokens, current.addChild(plusNode));
        }

        return current;
    }

    public Node<Word> parseWhole() {
        for (Map.Entry<Integer, LinkedList<KeyValue<Word, Token>>> lineEntry : tokens.entrySet()) {
            int line = lineEntry.getKey();
            LinkedList<KeyValue<Word, Token>> tokensInLine = lineEntry.getValue();

            rootExpression.addChild(parse(tokensInLine, null));
        }

        return rootExpression;
    }

    public Map<Integer, LinkedList<KeyValue<Word, Token>>> getTokens() {
        return tokens;
    }
}
