package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.dto.auth.UserCreateDto;
import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.exception.EntityNotFoundException;
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
        UserLoginDto userLoginDto = toObject(body, UserLoginDto.class);
        UserLoggedInDto userLoggedInDto = this.authService.createToken(userLoginDto);
        return json(userLoggedInDto, Status.ACCEPTED);
    }

    private Response createUser(String body) {
        UserCreateDto userCreateDto = toObject(body, UserCreateDto.class);
        User user = this.authService.register(userCreateDto);
        return json(user, Status.CREATED);
    }
}
