package gametree;

/**
 * Interface for classes that can be evaluated to a numeric value.
 * <br><br>
 * Evaluable offer 3  value levels:
 * <ul>
 *      <li>computed static values</li>
 *      <li>computed values of leaves ("leaf values")</li>
 *      <li>values that were explicitly set</li>
 * </ul>
 * Values supersede each other in this order.
 * <p>
 * Any value computation stores the determined value. This value can then
 * be retrieved by {@link #getValue()}.
 * In most cases the more specific methods
 * {@link #computeOrGetStaticValueOrBetter()} and {@link #computeOrGetLeafValueOrBetter(int)}
 * should be used to get values from Evaluables.
 * These methods guarantee to return a value of the expected level (static or leaf) or "better".
 * Any value that is of higher level (lower in the list) is considered to be better and will be
 * returned instead. If no suitable value (of demanded or higher level) is available, the value
 * of the demanded level will be computed, stored and returned.
 * <p>
 * If only explicitly set values should be retrieved, use
 * {@link #getExplicitValue()} which throws an Exception if the value was not set by {@link #setValue(int)}.
 */
public interface Evaluable {

    /**
    * This method simply returns the value that is currently stored by this Evaluable.
    * Values can be stored by calls to
    * {@link #computeOrGetStaticValueOrBetter()}, {@link #computeOrGetLeafValueOrBetter(int)} or {@link #setValue(int)}.
    * 
    * @return the value stored by this evaluable
    * @throws UninitializedValueException if this evaluable was not yet evaluated
    */
    public abstract int getValue() throws UninitializedValueException;

    /**
     * If a value was assigned to this Evaluable using {@link #computeOrGetLeafValueOrBetter(int)} 
     * or {@link #setValue(int)}, returns that value.
     * <p>
     * Otherwise, returns the statcc value of this Evaluable.
     * @return this Evaluable's static value or the value set by 
     * {@link #computeOrGetLeafValueOrBetter(int)} or {@link #setValue(int)}
     */
    public abstract int computeOrGetStaticValueOrBetter();

    /**
     * If a value was explicitly assigned to this Evaluable using {@link #setValue(int)},
     * returns that value.
     * <p>
     * Otherwise, evaluates this Evaluable while considering that it cannot generate any children
     * (is a terminal node). This static evaluation supersedes the one provided by
     * {@link #computeOrGetStaticValueOrBetter()}.
     * @param depth the depth of the leaf in the tree
     * @return the leaf's static evaluation or the explicitly set value
     */
    public abstract int computeOrGetLeafValueOrBetter(int depth);

    /**
     * @return the value of this Evaluable if it was set explicitly
     * @throws UninitializedValueException if no value was set explicitly
     */
    public abstract int getExplicitValue() throws UninitializedValueException;

    /**
     * Overwrites the current value of this evaluable with the specified value.
     * @param value the value that should be stored
     */
    public abstract void setValue(int value);

}
