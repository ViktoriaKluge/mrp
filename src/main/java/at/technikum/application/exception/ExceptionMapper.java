package at.technikum.application.exception;

import at.technikum.server.http.ContentType;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

import java.util.HashMap;
import java.util.Map;

public class ExceptionMapper {

    private final Map<Class<?>, Response> map;

    public ExceptionMapper() {
        this.map = new HashMap<>();
    }

    public Response toResponse(Exception exception) {
        Response response = new Response();

        // nochmal nachlesen

        response = map.get(exception.getClass());

        if (null != response) {
            response.setBody(exception.getMessage());
            return response;
        }

        response.setBody(exception.getMessage());
        response.setStatus(Status.INTERNAL_SERVER_ERROR);
        response.setContentType(ContentType.TEXT_PLAIN);

        return response;
    }

    public void register(Class<?> clazz, Response response) {
        map.put(clazz, response);
    }
}
