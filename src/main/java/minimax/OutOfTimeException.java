package minimax;

/**
 * Thrown to indicate that something has taken longer than allowed.
 */
public class OutOfTimeException extends Exception {
    public OutOfTimeException() {
        super();
    }

    public OutOfTimeException(String message) {
        super(message);
    }

    public OutOfTimeException(Throwable causedBy) {
        super(causedBy);
    }

    public OutOfTimeException(String message, Throwable causedBy) {
        super(message, causedBy);
    }
}
