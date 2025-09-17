package at.technikum.echo;

import at.technikum.application.common.Application;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class EchoApplication implements Application {

    @Override
    public Response handle(Request request) {
        Response response = new Response();

        return response;
    }
}
