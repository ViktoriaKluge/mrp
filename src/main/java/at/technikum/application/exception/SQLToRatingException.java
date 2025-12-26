package at.technikum.application.exception;

public class SQLToRatingException extends RuntimeException {
    public SQLToRatingException(String message) {
        super(message);
    }
}
