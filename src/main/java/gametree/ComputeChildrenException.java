package gametree;

/**
 * Thrown to indicate that {@link Node#queryChildren()} could not generate children.
 */
public class ComputeChildrenException extends Exception {
    ComputeChildrenException(String message) {
        super(message);
    }

    ComputeChildrenException(Throwable causedBy) {
        super(causedBy);
    }

    ComputeChildrenException(String message, Throwable causedBy) {
        super(message, causedBy);
    }

}
