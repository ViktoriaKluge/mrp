package at.technikum.application.exception;

public class SQLToObjectException extends RuntimeException {
    public SQLToObjectException(String message) {
        super(message);
    }
}
