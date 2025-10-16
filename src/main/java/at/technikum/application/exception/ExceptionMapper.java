package at.technikum.application.exception;

import at.technikum.server.http.ContentType;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

import java.util.HashMap;
import java.util.Map;

public class ExceptionMapper {

    private final Map<Class<?>, Status> map;
    public ExceptionMapper() {
        this.map = new HashMap<>();
    }

    public Response toResponse(Exception exception) {
        Response response = new Response();
        Status status = map.get(exception.getClass());

        if (null == status) {
            status = Status.INTERNAL_SERVER_ERROR;
        }

        response.setBody(exception.getMessage());
        response.setStatus(status);
        response.setContentType(ContentType.TEXT_PLAIN);

        return response;
    }

    public void register(Class<?> clazz, Status status) {
        map.put(clazz, status);
    }
}
