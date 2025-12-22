package at.technikum.application.common;

import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.exception.IncompatiblePayloadTypeException;
import at.technikum.application.exception.JsonConversionException;
import at.technikum.application.exception.NotJsonBodyException;
import at.technikum.server.http.ContentType;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Controller {

    public abstract Response handle(RequestDto requestDto);

    protected Response ok() {
        return status(Status.OK);
    }

    protected Response status(Status status) {
        return text(status.getMessage(),status);
    }

    protected Response text(String text, Status status) {
        return r(status, ContentType.TEXT_PLAIN,text);
    }

    protected Response json(Object o, Status status) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(o);
            return r(status, ContentType.APPLICATION_JSON, json);
        } catch (Exception ex) {
            throw new JsonConversionException(ex);
        }
    }

    protected Response r(Status status, ContentType contentType, String body) {
        Response response = new Response();
        response.setStatus(status);
        response.setBody(body);
        response.setContentType(contentType);

        return response;
    }

    protected static final ObjectMapper MAPPER = new ObjectMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected <T> T toOtherObject(RequestDto requestDto, Class<T> target) {
        try {
            return MAPPER.convertValue(requestDto, target);
        } catch (Exception ex) {
            throw new IncompatiblePayloadTypeException("Incompatible type for dto");
        }
    }

}
