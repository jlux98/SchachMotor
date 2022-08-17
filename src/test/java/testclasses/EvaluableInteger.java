package testclasses;

import positionevaluator.Evaluable;

/**
 * Class used to mock evaluables.
 * <br><br>
 * Instead of actually calculating a value, this class simply stores an Integer
 * and returns it to calls of getValue() and evaluate().
 */
public class EvaluableInteger implements Evaluable {

    private Integer value;
    private boolean interesting;

    public EvaluableInteger(int value) {
        this.value = value;
        this.interesting = false;
    }

    /**
     * Does not actually evaluate this Evaluable but simply returns its stored value.
     * Same as getValue().
     * Unmarks nodes as specified by {@link Evaluable}.
     * @return the value of this evaluable
     */
    @Override
    public int evaluate() {
        this.interesting = false;
        return getValue();
    }

    /**
    * This method does not evaluate this Evaluable, it simply returns the value that is currently stored by it.
    * Values can be stored by calls to setValue().
    * @return the value stored by this evaluable
    */
    @Override
    public int getValue() {
        return value;
    }

    /**
     * Overwrites the current value of this evaluable with the specified value.
     * @param value the value that should be stored
     */
    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean isInteresting() {
        return interesting;
    }

    @Override
    public void markAsInteresting() {
        this.interesting = true;
    }

}
