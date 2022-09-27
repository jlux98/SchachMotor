package positionevaluator;

import gametree.UninitializedValueException;

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
//TODO update class doc
public interface Evaluable {

    /**
     * If a value was assigned to this Evaluable using {@link #evaluateKnownLeafStatically(int)} 
     * or {@link #setValue(int)}, returns that value.
     * <p>
     * Otherwise, returns the statcc value of this Evaluable.
     * @return this Evaluable's static value or the value set by 
     * {@link #evaluateKnownLeafStatically(int)} or {@link #setValue(int)}
     */
    public abstract int getOrComputeValue();

    /**
     * If a value was explicitly assigned to this Evaluable using {@link #setValue(int)},
     * returns that value.
     * <p>
     * Otherwise, evaluates this evaluable statically while considering that it cannot generate any children
     * (is a terminal node). This static evaluation is more specific than the one provided by
     * {@link #getOrComputeValue()}.
     * @param depth the depth of the leaf in the tree
     * @return the leaf's static evaluation or the explicitly set value
     */
    public abstract int evaluateKnownLeafStatically(int depth);

    /**
     * This method simply returns the value that is currently stored by this Evaluable.
     * Values can be stored by calls to
     * {@link #getOrComputeValue()}, {@link #evaluateKnownLeafStatically(int)} or {@link #setValue(int)}.
     * 
     * @return the value stored by this evaluable
     * @throws UninitializedValueException if this evaluable was not yet evaluated
     */
    public abstract int getValue() throws UninitializedValueException;

    /**
     * Overwrites the current value of this evaluable with the specified value.
     * @param value the value that should be stored
     */
    public abstract void setValue(int value);

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

}
