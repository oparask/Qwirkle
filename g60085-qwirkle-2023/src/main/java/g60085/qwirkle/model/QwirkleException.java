package g60085.qwirkle.model;

/**
 * Exception thrown when an add request does not respect the Qwirkle game rules.
 */
public class QwirkleException extends RuntimeException {
    /**
     * Constructs a new QwirkleException with the specified error message.
     *
     * @param message the error message that describes the exception.
     */
    public QwirkleException(String message) {
        super(message);
    }
}
