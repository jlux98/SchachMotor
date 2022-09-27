package classes;

/**
 * Thrown to indicate that a test failed due to an checked exception.
 */
public class FailureException extends RuntimeException {
    public FailureException(String message) {
        super(message);
    }

    public FailureException(Throwable causedBy) {
        super(causedBy);
    }

    public FailureException(String message, Throwable causedBy) {
        super(message, causedBy);
    }
}
