package classes;

/**
 * An implementation of IntNode allowing different values
 * for static and leaf evaluation to be set in its constructor.
 */
public class DifferentValuesIntNode extends IntNode {

    public int staticValue;
    public int leafValue;

    /**
     * Creates a node that is evaluated to the specified values.
     * @param staticValue the value that {@link #computeStaticValue()} should return
     * @param leafValue the value {@link #computeStaticLeafValue(int)} should return
     */
    public DifferentValuesIntNode(int staticValue, int leafValue) {
        super(0);
        this.staticValue = staticValue;
        this.leafValue = leafValue;
    }

    @Override
    protected int computeStaticValue() {
        return staticValue;
    }

    @Override
    protected int computeStaticLeafValue(int depth) {
        return leafValue;
    }
}
