package at.technikum.application.common;

import at.technikum.server.http.ContentType;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

public abstract class Controller {

    public abstract Response handle(Request request);

    protected Response ok() {
        return status(Status.OK);
    }

    protected Response status(Status status) {
        return text(status.getMessage(),status);
    }

    protected Response text(String text, Status status) {
        return r(status, ContentType.TEXT_PLAIN,text);
    }

    protected Response r(Status status, ContentType contentType, String body) {
        Response response = new Response();
        response.setStatus(status);
        response.setBody(body);
        response.setContentType(contentType);

        return response;
    }

    protected Response text(String text) {
        return text(text, Status.OK);
    }
}
