package gametree;

/**
 * Thrown to indicate that {@link Node#queryChildren()} could not generate children.
 */
public class ComputeChildrenException extends Exception {
    public ComputeChildrenException(String message) {
        super(message);
    }

    public ComputeChildrenException(Throwable causedBy) {
        super(causedBy);
    }

    public ComputeChildrenException(String message, Throwable causedBy) {
        super(message, causedBy);
    }

}
