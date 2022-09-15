package positionevaluator;

/**
 * Interface for classes that can be evaluated to a numeric value.
 * <br><br>
 * Classes must be able to evaluate themselves but must store the determined
 * value after a call to {@link #evaluateStatically()}
 * so it can be overwritten by {@link #setValue(int)}.
 * The method {@link #getValue()} is used to retrieve the current value, whether
 * it has been set by {@link #evaluateStatically()}
 * or by explicitly assigning a value by calling {@link #setValue(int)}.
 * <br><br>
 * Nodes can be marked as especially interesting by {@link #markAsInteresting()}
 * and are unmarked by {@link #unmarkAsInteresting()}.
 * Use {@link #isInteresting()} to determine whether a node is currently marked
 * as interesting.
 */
public interface Evaluable {

    /**
     * Evaluates this Evaluable and sets its value accordingly.
     * <br><br>
     * This overwrites this Evaluable's current value with the determined value such that
     * calling {@link #getValue()} after calling {@link #evaluateStatically()} will return the same value.
     * @param depth
     * @param b
     * @return the calculated value of this evaluable
     */
    public abstract int evaluate(boolean isNaturaLeaf, int depth);

    /**
     * Evaluates this evaluable and returns the determined value.
     * The resultmust be the same for any invocation
     * of {@link #cachedEvaluateStatically()} on the same Evaluable
     *  and should be cached.
     * <p>
     * Does <b>not</b> overwrite the stored value.
     * @return the static value of this evaluable
     */
    public abstract int cachedEvaluateStatically();

    /**
     * Used to determine if this node is marked as especially interesting.
     * Nodes are marked by calls to {@link #markAsInteresting()}
     * and unmarked by {@link #unmarkAsInteresting()}.
     * Nodes that are marked this way may be priotized in evaluation strategies.
     * @return whether this node is marked as interesting.
     */
    public abstract boolean isInteresting();

    /**
     * Marks this Evaluable as especially interesting.
     * Evaluable that are marked this way may be priotized in evaluation strategies.
     */
    public abstract void markAsInteresting();

    /**
     * Unmarks this Evaluable if it was previously marked as especially interesting.
     * If this Evaluable was not marked, does nothing.
     */
    public abstract void unmarkAsInteresting();

    /**
     * This method does not evaluate this Evaluable, it simply returns the
     * value that is currently stored by it.
     * Values can be stored by calls to evaluateStatically() or setValue().
     * @return the value stored by this evaluable
     */
    public abstract int getValue();

    /**
     * Overwrites the current value of this evaluable with the specified value.
     * <br><br>
     * <ul>
     *      <li>
     *          Calling getValue() afterwards will return that value.
     *      </li>
     *      <li>
     *          Calling evaluateStatically() afterwards will <b>not</b> return that value
     *          but will evaluate this Evaluable and overwrite the value set by this method.
     *      </li>
     * </ul>
     * @param value the value that should be stored
     */
    public abstract void setValue(int value);

}
