package classes;

import gametree.BaseNode;
import gametree.ComputeChildrenException;
import gametree.Node;
import gametree.TreeEvaluator;

/**
 * Class used to mock instances of Node.
 * This class can be used to test the methods implemented by BaseNode.
 * and to test the implementation of AlphaBetaPruning.
 * BaseNode is an abstract class and thus can't be used for testing.
 */
public class IntNode extends BaseNode<Integer>  {

    /**
     * Creates an IntNode storing the specified value as content.
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

    @Override
    public int evaluateStatically() {
        this.setValue(getContent());
        return this.getContent();
    }

}
