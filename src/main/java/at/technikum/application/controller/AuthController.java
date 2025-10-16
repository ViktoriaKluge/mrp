package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.dto.UserCreate;
import at.technikum.application.dto.UserLoggedIn;
import at.technikum.application.dto.UserLogin;
import at.technikum.application.enums.UserType;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.User;
import at.technikum.application.service.AuthService;
import at.technikum.server.http.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

public class AuthController extends Controller {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Response handle(Request request) {
        String path = request.getPath();
        String body = request.getBody();
        String method = request.getMethod();

        if (path.equals("/auth")) {
            Response response = new Response();
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody("Auth overview");

            return  response;
        }

        if (method.equals(Method.POST.getVerb())) {

            if (path.endsWith("/login")) {
                return login(body);
            }

            if (path.endsWith("/register")) {
                return createUser(body);
            }
        }

        throw new EntityNotFoundException("Path not found");
    }

    private Response login(String body) {
        UserLogin userLogin = toObject(body, UserLogin.class);
        UserLoggedIn userLoggedIn = this.authService.createToken(userLogin);
        return json(userLoggedIn, Status.ACCEPTED);
    }

    private Response createUser(String body) {
        UserCreate userCreate = toObject(body, UserCreate.class);
        User user = this.authService.register(userCreate);
        return json(user, Status.CREATED);
    }
}
