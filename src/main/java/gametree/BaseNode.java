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
    private boolean isInteresting;
    private boolean staticEvaluationCached = false;
    private int staticValue;

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
        if (this.parent != null) {
            throw new IllegalStateException(
                    "a node can only be child to a single node, this node already has a parent");
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
     * This is a helper method that is implemented individually by subtypes and called by {@link #queryChildren()}.
     * Use queryChildren() to generate children of this node.
     * </p>
     * @throws ComputeChildrenException if no children can be computed
     */
    protected abstract void computeChildren() throws ComputeChildrenException;

    @Override
    public List<? extends Node<T>> queryChildren() throws ComputeChildrenException {
        if (!hasChildren()) {
            computeChildren();
            detachChildGenerationData();
        }
        return children;
    }

    /**
     * Hook for subclasses.
     * Called after {@link #computeChildren()} in {@link #queryChildren()}.
     * Intended to allow for deletion of data from the node that is
     * only required to generate children.
     */
    protected void detachChildGenerationData() {
        //do nothing
    }

    /**
     * Evaluates this node statically and returns the determined value.
     * <p>
     * <b>Note:</b> Do not use this method directly to evaluate this node statically.
     * This is a helper method that is implemented individually by subtypes and called by 
     * {@link #evaluateStatically(boolean, int)} and {@link #roughlyEvaluateStatically()}.
     * Use {@link #evaluateStatically(boolean, int)} or {@link #roughlyEvaluateStatically()} to evaluate this node statically.
     * </p>
     * @param isNaturaLeaf
     * @param depth
     * @return the static evaluation of this node
     */
    protected abstract int computeStaticValue(boolean isNaturaLeaf, int depth);

    @Override
    public int roughlyEvaluateStatically() {
        PerformanceData.roughlyEvaluateStaticallyCalls += 1;
        if (!staticEvaluationCached) {
            //only compute static value once
            //compute static value with depth = 0 and non-leaf
            staticValue = computeStaticValue(false, 0);
            staticEvaluationCached = true;
        }
        value = staticValue; //overwrite "general" value
        return staticValue;
    }

    @Override
    public int evaluateStatically(boolean isNaturaLeaf, int depth) {
        PerformanceData.evaluateStaticallyCalls += 1;
        //overwrite value
        value = computeStaticValue(isNaturaLeaf, depth);
        return value;
    }

    /**
     * This method is intended <b>for testing only.</b>
     * Do not use this to retrieve the children of a node.
     * <br><br>
     * Use {@link #queryChildren()} instead.
     * @return this nodes stored children
     */
    protected List<? extends Node<T>> getChildren() {
        return this.children;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(int value) {
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

    @Override
    public String toString() {
        return getContent().toString();
    }

}
