package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.model.User;
import at.technikum.application.service.UserService;
import at.technikum.server.http.*;

import java.util.Optional;

public class UserController extends Controller {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response handle(Request request) {
        String[] path = request.getPath().split("/");
        String method = request.getMethod();
        String body = request.getBody();

        if (path.length == 2) {
            Response response = new Response();
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody("Users overview");
            return response;
        }

        if (method.equals(Method.GET.getVerb())) {
            if (path[3].equals("profile")) {
                return profile(path[2]);
            }

            if (path[3].equals("ratings")) {
                return ratings(path[2]);
            }

            if (path[3].endsWith("favorites")) {
                return favorites(path[2]);
            }
        }

        if (method.equals(Method.PUT.getVerb())) {
            if (path[3].equals("profile")) {
                return update(path[2], body);
            }
        }

        if (method.equals(Method.DELETE.getVerb())) {
            return delete(path[2]);
        }

        // return notFound() ? oder Exception?
        throw new RuntimeException("404");
    }

    private Optional<User> findUser(String id) {
        return this.userService.getUser(id);
    }

    private Response profile(String id) {
        Response response = new Response();
        Optional<User> user = findUser(id);

        if (user.isEmpty()) {
            response.setStatus(Status.NOT_FOUND);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody("User not found");

            return response;
        }

        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("Profil: "+findUser(id).toString());

        return response;
    }

    private Response ratings(String id) {
        Response response = new Response();

        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody(this.userService.ratings(id).toString());

        return response;
    }

    private Response favorites(String id) {
        Response response = new Response();

        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody(this.userService.favorites(id).toString());

        return response;
    }

    private Response update(String id, String body) {
        Response response = new Response();

        // body in user-daten aufteilen
        User update = new User();
        Optional<User> user = this.userService.update(id,update);

        if (user.isEmpty()) {
            response.setStatus(Status.NOT_FOUND);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody("User not found");

            return response;
        }

        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("User updated successfully "+ user.toString());

        return response;
    }

    private Response delete(String id) {
        Response response = new Response();

        // this.userService.delete(id);

        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("User deleted successfully");

        return response;
    }
}
