package at.technikum.server.http;

public enum Status {

    // success
    OK (200, "OK"),
    CREATED (201, "Created"),
    ACCEPTED (202, "Accepted"),

    // client error
    BAD_REQUEST (400, "Bad Request"),
    UNAUTHORIZED (401, "Unauthorized"),
    FORBIDDEN (403, "Forbidden"),
    NOT_FOUND (404, "Not Found"),
    CONFLICT (409, "Conflict"),
    UNPROCESSABLE_ENTITY (422, "Unprocessable Entity"),

    // server error
    INTERNAL_SERVER_ERROR (500, "Internal Server Error"),
    ;

    private final int code;
    private final String message;

    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
