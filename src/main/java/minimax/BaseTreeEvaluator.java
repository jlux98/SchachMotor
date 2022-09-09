package minimax;

/**
 * Abstract class implementing TreeEvaluator,
 * providing partial implementation of the counter
 * for evaluated nodes.
 * <p>
 * Extending classes must call {@link #increaseEvaluatedNodeCount()}
 * whenever evaluating a node.
 * </p>
 */
public abstract class BaseTreeEvaluator<T> implements TreeEvaluator<T> {

    private int evaluatedNodeCount = 0;

    /**
     * Increases the evaluated node counter by one.
     */
    protected void increaseEvaluatedNodeCount() {
        this.evaluatedNodeCount += 1;
    }

    @Override
    public int getEvaluatedNodeCount() {
        return evaluatedNodeCount;
    }

    @Override
    public void resetEvaluatedNodeCount() {
        this.evaluatedNodeCount = 0;
    }
    
}
