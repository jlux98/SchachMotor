package positionevaluator;

/**
 * Interface for classes that can be evaluated to a numeric value.
 * Classes must be able to evaluate themselves but must store the determined value after a call to {@link #evaluate()}
 * so it can be overwritten by {@link #setValue(int)}.
 *The method {@link #getValue()} is used to retrieve the current value, whether it has been set by evaluate()
 * or by explicitly assigning a value by calling setValue(int).
 */
public interface Evaluable {

    /**
     * Evaluates this Evaluable and sets its value accordingly.
     * <br><br>
     * This overwrites this Evaluable's current value with the determined value such that
     * calling getValue() after calling evaluate() will return the same value.
     * @return the calculated value of this evaluable
     */
    public abstract int evaluate();

    /**
     * This method does not evaluate this Evaluable, it simply returns the value that is currently stored by it.
     * Values can be stored by calls to evaluate() or setValue().
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
     *          Calling evaluate() afterwards will <b>not</b> return that value but will evaluate this Evaluable
     *          and overwrite the value set by this method.
     *      </li>
     * </ul>
     * @param value the value that should be stored
     */
    public abstract void setValue(int value);

}
