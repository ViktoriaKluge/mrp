package at.technikum.server.http;

import com.sun.net.httpserver.HttpExchange;

public class Request {

    private String method;

    private String path;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
