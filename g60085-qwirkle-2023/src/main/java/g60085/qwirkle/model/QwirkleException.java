package g60085.qwirkle.model;

/**
 * As soon as an add request does not respect the Qwirkle game rules, an exception is thrown;
 */
public class QwirkleException extends RuntimeException {
    /**
     * @param message Message that appears when throwing the Qwirkle exception;
     */
    public QwirkleException(String message) {
        super(message);
    }
}
