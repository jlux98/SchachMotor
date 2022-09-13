package uciservice;

/**
 * Exception throw by {@link FenParser} if a Fen-String
 * cannot be parsed to a position.
 */
public class FenParseException extends RuntimeException {

    public FenParseException(String message) {
        super(message);
    }

    public FenParseException(Throwable exception) {
        super(exception);
    }

    public FenParseException(String message, Throwable exception) {
        super(message, exception);
    }
}
