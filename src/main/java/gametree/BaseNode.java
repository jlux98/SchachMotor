package gametree;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class providing a basic node implementation.
 * The only abstract method subtypes have to implement is {@link computeChildren()}
 * which is used to generate children of a node to grow a tree as needed.
 */
public abstract class BaseNode<T> implements Node<T> {

    private Node<T> parent;
    private T content;
    private List<Node<T>> children;

    /**
     * Creates a root node.
     * @param content content stored by the node
     */
    public BaseNode(T content) {
        this.content = content;
    }

    /**
     * Creates a child node.
     * @param content content stored by the node
     * @param parent parent of the created node
     */
    public BaseNode(T content, Node<T> parent) {
        this(content);
        this.parent = parent;
        // other attributes are null here
    }

    /**
     * Creates a list to store children of this node, if it does not yet exist.
     */
    protected void createChildListIfNotExists() {
        if (this.children == null) {
            this.children = new ArrayList<Node<T>>(20);
        }
    }

    @Override
    public void deleteChild(Node<T> node) {
        // removing with ArrayList.remove(node) would require gamenode.equals()
        // which would probably have to compare positions which is inefficient

        // removing by equals also does not guarantee removal of the correct node if 2
        // equal nodes are present,
        // though in that case either all or none of the nodes should be removed
        Node<T> child = null;
        for (int i = 0; i < children.size(); i++) {
            child = children.get(i);
            if (child == node) {
                children.remove(i);
                child.setParent(null);
                return;
            }
        }
        throw new IllegalArgumentException("child to remove could not be found");
    }

    @Override
    public void deleteChildren() {
        for (Node<T> child : children) {
            child.setParent(null);
        }
        this.children.clear();
    }

    @Override
    public void deleteSelf() {
        try {
            this.parent.deleteChild(this);
        } catch (IllegalArgumentException exception) {
            //if this node cannot be found as a child of it's parent,
            //it is deemed to already have been removed -> void exception
        }
    }

    @Override
    public T getContent() {
        return content;
    }

    @Override
    public Node<T> getParent() {
        return this.parent; //possibly null
    }

    /**
    * Whether this node has children. Note that returning false does not imply that this note cannot generate 
    * children when calling {@link #queryChildren()}.
    * @return true - if this node currently has any children, false - if not
    */
    @Override
    public boolean hasChildren() {
        return this.children != null && this.children.size() != 0;
    }

    /**
     * Adds a child node to this node.
     * <br><br>
     * <b>Note:</b>
     * Only type safe if only GameNodes and subtypes of GameNode are passed into this method.
     * Otherwise a class cast exception might arise.
     */
    @Override
    public void insertChild(Node<T> node) {
        createChildListIfNotExists();
        node.setParent(this);
        this.children.add(node);

    }

    @Override
    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    /**
    * Computes this node's children and overwrites its current child list accordingly.
    * <br><br>
    * <b>Note:</b> Do not use this method directly to generate children of this node.
    * This is a helper method that is implemented individually by subtypes and called by {@link #queryChildren()}.
    * Use queryChildren() to generate children of this node.
    */
    protected abstract void computeChildren();

    @Override
    public List<? extends Node<T>> queryChildren() {
        if (!hasChildren()) {
            computeChildren();
        }
        return children;
    }

}
