package classes;

import java.util.List;

import gametree.BaseNode;
import gametree.ComputeChildrenException;
import gametree.Node;
import minimax.TreeEvaluator;

/**
 * Class used to mock instances of Node.
 * This class is a subtype of Node < Integer > and implements
 * {@link #evaluateStatically()} to return the stored integer as
 * static evaluation.
 * <br><br>
 * This class can be used to test the methods implemented by BaseNode.
 * and to test the implementation of AlphaBetaPruning.
 * BaseNode is an abstract class and thus can't be used for testing.
 * <br><br>
 * IntNodes store integers as node content and can be evaluated
 * to an int value.
 * {@link #evaluateStatically()} will always return the value of
 * the stored integer as node value.
 * However, the IntNode's value can be changed by calling
 * {@link #setValue(int)} (f.ex. by alpha-beta-pruning) and
 * does not necessarily stay the same as the stored content.
 */
public class IntNode extends BaseNode<Integer> {

    /**
     * Creates an IntNode storing the specified Integer as content.
     * @param content
     */
    public IntNode(Integer content) {
        super(content);
    }

    /**
     * Creates an IntNode with the specified content and parent.
     * The nodes are properly linked to each other by this constructor.
     * @param content
     * @param parent
     */
    public IntNode(Integer content, Node<Integer> parent) {
        super(content, parent);
    }

    @Override
    //narrows return type from Node<T> to GameNode
    public IntNode getParent() {
        return (IntNode) super.getParent();
    }

    /**
     * Not supported by IntNode.
     * <br><br>
     * This means that trees consisting of IntNodes cannot be dynamically deepened.
     * Thus trees consisting of IntNodes must be constructed to reach the desired depth
     * before passing them to a method like {@link TreeEvaluator#evaluateTree(gametree.Tree, int, boolean)}
     */
    @Override
    protected void computeChildren() throws ComputeChildrenException {
        throw new ComputeChildrenException("int node cannot dynamically generate children");
    }

    /**
     * Evaluates this IntNode statically by setting and 
     * returning the integer that is stored as content as this node's value.
     */
    @Override
    public int evaluateStatically(boolean isNaturalLeaf, int depth) {
        this.setValue(getContent());
        return this.getContent();
    }

    @Override
    protected int computeStaticValue(boolean isNaturaLeaf, int depth) {
        //set content as value and staticValue
        return getContent();
    }

    @Override
    public int roughlyEvaluateStatically() {
        //overwrite any caching mechanisms so changes to stored content are reflected by value
       return this.evaluateStatically(false, -1);
    }

    @Override
    public Node<Integer> createChild(Integer content) {
        return new IntNode(content, this);
    }

    /**
     * Compares the value stored in this intnode with the one
     * stored in the other intnode.
     * @param obj the IntNode that this IntNode should be compared to
     * @return true if the value of both IntNodes is equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof IntNode) {
            return this.getContent() == ((IntNode) obj).getContent();
        }
        return false;
    }

    //increase visibility to public in this class
    @Override
    public List<? extends Node<Integer>> getChildren() {
        return super.getChildren();
    }

}
