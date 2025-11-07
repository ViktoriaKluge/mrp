package at.technikum.application.exception;

public class SQLToUserException extends RuntimeException {

    public SQLToUserException() {
    }

    public SQLToUserException(String message) {
        super(message);
    }

    public SQLToUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLToUserException(Throwable cause) {
        super(cause);
    }

    public SQLToUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
