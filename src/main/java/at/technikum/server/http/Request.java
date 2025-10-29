package at.technikum.server.http;

import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.NotAuthorizedException;
import at.technikum.application.exception.NotJsonBodyException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class Request {

    private Method method;
    private String path;
    private String body;
    private Map<String, List<String>> headers;

    public Request() {

    }

    public RequestDto getRequestDto(){

        RequestDto requestDto =toObject();
        requestDto.setToken(getBearerToken());
        requestDto.setMethod(this.method);
        requestDto.setPath(this.path.split("/"));
        return requestDto;
    }

    private String getHeader(String name) {
       List<String> list = headers.get(name);
       if (list == null || list.isEmpty()) {
           return "No header found";
       }
       return list.getFirst();
    }

    private String getBearerToken() {
        String authHeader = getHeader("Authorization");
        String[] parts = authHeader.trim().split(" ", 2);
        if (parts[0].equals("Bearer")) {
            return parts[1];
        }
        return "None";
    }

    private RequestDto toObject() {
        if (this.body == null || this.body.isEmpty()) {
            return new RequestDto();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(this.body, RequestDto.class);
            } catch (Exception ex) {
                throw new NotJsonBodyException("ToObject doesnt work");
            }
        }
    }

    public String getMethod() {
        return method.getVerb();
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }
}
