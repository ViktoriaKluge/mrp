package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.dto.UserCreate;
import at.technikum.application.dto.UserUpdate;
import at.technikum.application.dto.UserUpdated;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;
import at.technikum.application.service.UserService;
import at.technikum.server.http.*;

import java.util.ArrayList;
import java.util.List;
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

        throw new EntityNotFoundException("Path not found");
    }

    private Response profile(String id) {
        User user = this.userService.getUser(id);
        return json(user,Status.OK);
    }

    private Response ratings(String id) {
        List<Rating> ratings = this.userService.ratings(id);
        return json(ratings, Status.OK);
    }

    private Response favorites(String id) {
        List<Media> favorites = this.userService.favorites(id);
        return json(favorites, Status.OK);
    }

    private Response update(String id, String body) {
        UserUpdate userUpdate = toObject(body, UserUpdate.class);
        UserUpdated user = this.userService.update(id,userUpdate);
        return json(user,Status.OK);
    }

    private Response delete(String id) {
        String username = this.userService.delete(id);
        return json(username,Status.OK);
    }
}
