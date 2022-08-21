package classes;

import java.util.List;

import gametree.ComputeChildrenException;
import gametree.Node;

/**
 * A subtype of IntNode able to generate child nodes.
 */
public class GeneratingIntNode extends IntNode {
    /**
     * the amount of children that this node will generate upon calling  computeChildren()
     */
    private int childCount;
    public GeneratingIntNode(Integer value, int childCount) {
        super(value);
        this.childCount = childCount;
    }

    public GeneratingIntNode(Integer value, Node<EvaluableInteger> parent, int childCount) {
        super(value, parent);
        this.childCount = childCount;
    }

    @Override
    protected void computeChildren() throws ComputeChildrenException {
        if (childCount == 0) {
            throw new ComputeChildrenException("node value = 0");
        }
        //add children  that can generate between 0 and n-1 children to this node
        for (int i = 0; i < childCount; i++) {
            new GeneratingIntNode(0, this, i);
        }
    }

    //increase visibility to public in this class
    @Override
    public List<? extends Node<EvaluableInteger>> getChildren() {
        // TODO Auto-generated method stub
        return super.getChildren();
    }
}
