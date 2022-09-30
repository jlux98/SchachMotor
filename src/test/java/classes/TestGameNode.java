package classes;

import gametree.ComputeChildrenException;
import gametree.GameNode;
import model.Position;

/**
 * An implementation of GameNode used in testing.
 * Invocations of {@link #computeChildren()} increase the counter computeChildrenCalls, 
 * which is used to verify the storing of results in {@link #getOrComputeChildren()}.
 */
public class TestGameNode extends GameNode {
    public int computeChildrenCalls = 0;

     /**
     * Creates a test node.
     * @param position position stored by the node
     */
    public TestGameNode(Position position) {
        super(position);
    }
    
    @Override
    protected void computeChildren() throws ComputeChildrenException {
        computeChildrenCalls++;
        super.computeChildren();
    }
}
