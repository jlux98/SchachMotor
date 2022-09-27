package gametree;

/**
 * Thrown to indicate that reading the value of {@link positionevaluator.Evaluable} was attempted without
 * ever evaluating it.
 */
public class UninitializedValueException extends Exception {
    public UninitializedValueException(String message) {
        super(message);
    }

    public UninitializedValueException(Throwable causedBy) {
        super(causedBy);
    }

    public UninitializedValueException(String message, Throwable causedBy) {
        super(message, causedBy);
    }
}
