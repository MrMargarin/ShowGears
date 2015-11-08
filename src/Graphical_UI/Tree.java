package Graphical_UI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitrey on 28.10.2015.
 */
public class Tree<T> {
    private Node<T> root;

    public Tree(T rootData) {
        root = new Node<T>();
        root.setData(rootData);
        root.setChildren(new ArrayList<Node<T>>());
    }

    public static class Node<T> {
        private T data;
        private Node<T> parent;
        private List<Node<T>> children;

        public Node() {}
        public Node(T data) { this.setData(data);}
        public Node(T data, Node parent) { this.setData(data); this.setParent(parent);}
        public Node(T data, Node parent, List children) { this.setData(data); this.setParent(parent); this.setChildren(children);}


        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node<T> getParent() {
            return parent;
        }

        public void setParent(Node<T> parent) {
            this.parent = parent;
        }

        public List<Node<T>> getChildren() {
            return children;
        }

        public void setChildren(List<Node<T>> children) {
            this.children.add((Node<T>) children);
        }

        public void addChildren(Node<T> children) {
            this.children.add(children);
        }
    }

    public void addNodeToPar(Node parent, T data)
    {
        if(parent.getParent() == null) this.root.addChildren(new Node<T>(data, this.root));

        else
        {

        }
    }
}