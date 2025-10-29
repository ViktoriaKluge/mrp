package at.technikum.application.exception;

public class IncompatiblePayloadTypeException extends RuntimeException {
    public IncompatiblePayloadTypeException() {
    }

    public IncompatiblePayloadTypeException(String message) {
        super(message);
    }

    public IncompatiblePayloadTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompatiblePayloadTypeException(Throwable cause) {
        super(cause);
    }

    public IncompatiblePayloadTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
