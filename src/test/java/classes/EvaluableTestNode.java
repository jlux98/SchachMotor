package classes;

import gametree.Node;

/**
 * Test class used to test the implementation of Evaluable
 * provided by BaseNode.
 */
public class EvaluableTestNode extends IntNode {
    private int computeStaticValueCalls = 0;
    private int computeLeafValueCalls = 0;

    /**
     * Creates an EvaluableTestNode storing the specified Integer as content.
     * @param content
     */
    public EvaluableTestNode(Integer content) {
        super(content);
    }

    /**
     * Creates an EvaluableTestNode with the specified content and parent.
     * The nodes are properly linked to each other by this constructor.
     * @param content
     * @param parent
     */
    public EvaluableTestNode(Integer content, Node<Integer> parent) {
        super(content, parent);
    }

    @Override
    protected int computeStaticValue() {
        computeStaticValueCalls++;
        return getContent();
    }

    @Override
    protected int computeStaticLeafValue(int depth) {
        computeLeafValueCalls++;
        return getContent() + depth;
    }

    public int getComputeStaticValueCalls() {
        return computeStaticValueCalls;
    }
    
    public int getComputeLeafValueCalls() {
        return computeLeafValueCalls;
    }
}
