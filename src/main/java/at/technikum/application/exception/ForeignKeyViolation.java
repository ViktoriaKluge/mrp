package at.technikum.application.exception;

public class ForeignKeyViolation extends RuntimeException {
    public ForeignKeyViolation(String message) {
        super(message);
    }
}
