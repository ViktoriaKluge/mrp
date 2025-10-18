package at.technikum.server.http;

import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.NotAuthorizedException;

import java.util.List;
import java.util.Map;

public class Request {

    private Method method;
    private String path;
    private String body;
    private Map<String, List<String>> headers;
    private String bearerToken;

    public Request() {

    }

    public String getHeader(String name) {
       List<String> list = headers.get(name);
       if (list == null || list.isEmpty()) {
           throw new EntityNotFoundException("No such header " + name);
       }
       return list.getFirst();
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

    public String getBearerToken() {
        String authHeader = getHeader("Authorization");
        String[] parts = authHeader.trim().split(" ", 2);
        if (parts[0].equals("Bearer")) {
            return parts[1];
        }
        throw new NotAuthorizedException("Bearer token required");
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
