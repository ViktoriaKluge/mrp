package at.technikum.application.exception;

import at.technikum.server.http.Response;

public abstract class HttpStatusException extends RuntimeException {

    public abstract Response getResponse();
}
