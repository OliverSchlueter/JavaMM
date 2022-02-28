package de.oliver.javapp.utils;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    private final T data;
    private final List<Node<T>> children;

    public Node(T data, List<Node<T>> children) {
        this.data = data;
        this.children = children;
    }

    public Node(T data){
        this.data = data;
        this.children = new ArrayList<>();
    }

    public Node<T> addChild(Node<T> child){
        children.add(child);
        return this;
    }

    public void print(String indent){
        System.out.println(indent + (data != null ? data.toString() : ""));

        indent += "|  ";

        for (Node<T> child : children) {
            child.print(indent);
        }
    }

    public T getData() {
        return data;
    }

    public List<Node<T>> getChildren() {
        return children;
    }
}