package gametree;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import utility.PerformanceData;

/**
 * Abstract class providing a basic node implementation.
 * The only abstract methods subtypes have to implement are {@link #computeChildren()}
 * which is used to generate children of a node to grow a tree as needed,
 * and {@link #computeStaticValue(boolean, int)} used to evaluate nodes statically.
 */
public abstract class BaseNode<T> implements Node<T> {

    private Node<T> parent;
    private T content;
    private List<Node<T>> children;

    private int value;
    private boolean isStaticValueOrBetter = false;
    private boolean isStaticLeafValueOrBetter = false;
    private boolean isExplicitValue = false;
    private boolean isInteresting = false;

    /**
     * Creates a root node.
     * @param content content stored by the node
     */
    public BaseNode(T content) {
        this.content = content;
        //parent == null here
    }

    /**
     * Creates a child node.
     * The nodes are properly linked to each other by this constructor.
     * @param content content stored by the node
     * @param parent parent of the created node
     */
    public BaseNode(T content, Node<T> parent) {
        this(content);
        parent.insertChild(this);
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
                child.unsetParent();
                return;
            }
        }
        throw new NoSuchElementException("child to remove could not be found");
    }

    @Override
    public void deleteChildren() {
        if (hasChildren()) {
            for (Node<T> child : children) {
                child.unsetParent();
            }
            this.children.clear();
        }
    }

    /**
     * @throws NoSuchElementException if this node has a parent but it could not be deleted from it
     */
    @Override
    public void deleteSelf() {
        if (this.parent != null) {
            this.parent.deleteChild(this);
        }
    }

    @Override
    public T getContent() {
        return content;
    }

    @Override
    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public void deleteContent() {
        this.content = null;
    }

    @Override
    public Node<T> getParent() {
        return this.parent; //possibly null
    }

    /**
     * Whether this node has children. Note that returning false does not imply that this note cannot generate 
     * children when calling {@link #getOrComputeChildren()}.
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
        if (this.parent != null) {
            throw new IllegalStateException("a node can only be child to a single node, this node already has a parent");
        }
        this.parent = parent;
    }

    @Override
    public void unsetParent() {
        this.parent = null;
    }

    /**
     * Creates a child of this node.
     * The nodes are properly linked to each other.
     * <p>
     * Override this to control which type of GameNode is instantiated
     * by {@link #computeChildren()}.
     * </p>
     * @param position the position to be stored in the child node
     * @return a child of this node
     */
    public abstract Node<T> createChild(T content);

    /**
     * Computes this node's children and overwrites its current child list accordingly.
     * <p>
     * <b>Note:</b> Do not use this method directly to generate children of this node.
     * This is a helper method that is implemented individually by subtypes and called by {@link #getOrComputeChildren()}.
     * Use queryChildren() to generate children of this node.
     * </p>
     * @throws ComputeChildrenException if no children can be computed
     */
    protected abstract void computeChildren() throws ComputeChildrenException;

    @Override
    public List<? extends Node<T>> getOrComputeChildren() throws ComputeChildrenException {
        if (!hasChildren()) {
            computeChildren();
            detachChildGenerationData();
        }
        return children;
    }

    public List<? extends Node<T>> getChildren() {
        return this.children;
    }

    @Override
    public String toString() {
        return getContent().toString();
    }

    /**
     * Hook for subclasses.
     * Called after {@link #computeChildren()} in {@link #getOrComputeChildren()}.
     * Intended to allow for deletion of data from the node that is
     * only required to generate children.
     */
    protected void detachChildGenerationData() {
        //do nothing
    }

    //  *************************************
    //  *     evaluable functionality       *
    //  *************************************

    @Override
    public final int getValue() throws UninitializedValueException {
        if (isStaticValueOrBetter || isStaticLeafValueOrBetter || isExplicitValue) {
            return value;
        }
        throw new UninitializedValueException("this node was not yet evaluated");
    }

    @Override
    public final int computeOrGetStaticValueOrBetter() {
        PerformanceData.getOrComputeStaticValueCalls += 1;
        if (isStaticValueOrBetter) {
            return value;
        }
        isStaticValueOrBetter = true;
        value = computeStaticValue();
        return value;
    }

    /**
     * Computes the static value of this evaluable.
     * @return the static evaluation of this evaluable
     */
    protected abstract int computeStaticValue();

    @Override
    public final int computeOrGetLeafValueOrBetter(int depth) {
        PerformanceData.getOrComputeLeafValueCalls += 1;
        if (isStaticLeafValueOrBetter) {
            return value;
        }
        isStaticValueOrBetter = true;
        isStaticLeafValueOrBetter = true;
        value = computeStaticLeafValue(depth);
        return value;
    }

    /**
     * Compute the value of this evaluable statically while considering that it cannot generate any children
     * (is a terminal node). This static evaluation should be more specific than the one provided by
     * {@link #computeStaticValue()()}.
     * @param depth the depth of the leaf in the tree
     * @return the leaf's static evaluation
     */
    protected abstract int computeStaticLeafValue(int depth);

    @Override
    public int getExplicitValue() throws UninitializedValueException {
        if (isExplicitValue) {
            return value;
        }
        throw new UninitializedValueException("no value was set explicitly");
    }

    @Override
    public final void setValue(int value) {
        isExplicitValue = true;
        isStaticValueOrBetter = true;
        isStaticLeafValueOrBetter = true;
        this.value = value;
    }

    @Override
    public boolean isInteresting() {
        return isInteresting;
    }

    @Override
    public void markAsInteresting() {
        this.isInteresting = true;
    }

    @Override
    public void unmarkAsInteresting() {
        this.isInteresting = false;
    }

}
