package de.oliver.javapp.utils;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    private T data;
    private List<Node<T>> children;

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

    public void removeChild(Node<T> child){
        if(children.contains(child)){
            children.remove(child);
        }
    }

    public void print(String indent){
        System.out.println(indent + (data != null ? data.toString() : ""));

        indent += "|  ";

        /*for (Node<T> child : children) {
            child.print(indent);
        }*/
        for (int i = 0; i < children.size(); i++) {
            children.get(i).print(indent + "{" + i + "} ");
        }
    }

    public T getData() {
        return data;
    }

    public Node<T> setData(T data) {
        this.data = data;
        return this;
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public Node<T> setChildren(List<Node<T>> children) {
        this.children = children;
        return this;
    }

    public Node<T> getLastRight(){
        if(children.size() == 2){
            if(children.get(0).getChildren().size() != 2){
                if(children.get(1).getChildren().size() != 2) {
                    return this;
                }
            }
        }
        return children.get(1).getLastRight();
    }


    public Node<T> clone(){
        Node<T> newNode = new Node<>(data);
        for (Node<T> child : children) {
            newNode.addChild(child.clone());
        }
        return newNode;
    }
}