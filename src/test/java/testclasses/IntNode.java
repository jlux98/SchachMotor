package testclasses;

import gametree.BaseNode;
import gametree.ComputeChildrenException;
import gametree.Node;
import gametree.TreeEvaluator;

/**
 * Class used to mock instances of Node < Evaluable >.
 * This class can be used to test the methods implemented by BaseNode.
 * and to test the implementation of AlphaBetaPruning.
 * BaseNode is an abstract class and thus can't be used for testing.
 */
public class IntNode extends BaseNode<EvaluableInteger>  {

    public IntNode(Integer value) {
        super(new EvaluableInteger(value));
    }

    public IntNode(Integer value, Node<EvaluableInteger> parent) {
        super(new EvaluableInteger(value), parent);
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
        throw new UnsupportedOperationException("int node cannot dynamically generate children");
    }

}
