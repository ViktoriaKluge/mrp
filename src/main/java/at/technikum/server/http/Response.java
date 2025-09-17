package at.technikum.server.http;

public class Response {

    private String contentType;
    private String body;
    private int statusCode;

    public String getContentType() {
        return contentType;
    }

    public String getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
