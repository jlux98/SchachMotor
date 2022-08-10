package testclasses;

import gametree.BaseNode;
import gametree.Node;
import gametree.TreeEvaluator;
import positionevaluator.Evaluable;

/**
 * Class used to mock instances of Node < Evaluable >.
 * This class can be used to test the methods implemented by BaseNode.
 * and to test the implementation of AlphaBetaPruning.
 * BaseNode is an abstract class and thus can't be used for testing.
 */
public class IntNode extends BaseNode<EvaluableInteger> implements Evaluable {

    public IntNode(EvaluableInteger value) {
        super(value);
    }

    public IntNode(EvaluableInteger value, Node<EvaluableInteger> parent) {
        super(value, parent);
    }

    /**
     * Does not actually evaluate this node but simply returns its stored value.
     * Same as getValue().
     * @return the value of this node
     */
    @Override
    public int evaluate() {
        return getContent().evaluate();
    }

    /**
    * This method returns the value that is currently stored by this node.
    * Values can be stored by calls to setValue().
    * @return the value stored by this node
    */
    @Override
    public int getValue() {
        return getContent().getValue();
    }

    /**
    * Overwrites the current value of this evaluable with the specified value.
    * @param value the value that should be stored
    */
    @Override
    public void setValue(int value) {
        this.getContent().setValue(value);
    }

    /**
     * Not supported by IntNode.
     * <br><br>
     * This means that trees consisting of IntNodes cannot be dynamically deepened.
     * Thus trees consisting of IntNodes must be constructed to reach the desired depth
     * before passing them to a method like {@link TreeEvaluator#evaluateTree(gametree.Tree, int, boolean)}
     */
    @Override
    protected void computeChildren() {
        throw new UnsupportedOperationException("int node cannot dynamically generate children");
    }

}
