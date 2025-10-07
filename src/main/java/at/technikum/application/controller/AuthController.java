package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.model.User;
import at.technikum.application.service.AuthService;
import at.technikum.server.http.*;

public class AuthController extends Controller {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Response handle(Request request) {
        String path = request.getPath();
        String body = request.getBody();

        if (path.equals("/auth")) {
            Response response = new Response();
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody("Auth overview");

            return  response;
        }

        if (request.getMethod().equals(Method.POST.getVerb())) {

            if (path.endsWith("/login")) {
                return login(body);
            }

            if (path.endsWith("/register")) {
                return register(body);
            }
        }

        throw new RuntimeException("404");
    }

    private Response login(String body) {
        Response response = new Response();

        // noch mockup, später aus body
        String password = "password";
        this.authService.login(body, password);

        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("User logged in "+body);

        return response;
    }

    private Response register(String body) {
        Response response = new Response();
        User user = new User();
        //später aus body

        this.authService.register(user);

        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("User registered");

        return response;
    }
}
