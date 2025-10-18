package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.dto.media.MediaListAuthorizedDto;
import at.technikum.application.dto.rating.RatingListAuthorizedDto;
import at.technikum.application.dto.users.*;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.service.UserService;
import at.technikum.server.http.*;

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
        String token = request.getBearerToken();

        if (path.length == 2) {
            Response response = new Response();
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody("Users overview");
            return response;
        }

        UserAuthorizeDto userAuthorizeDto = new UserAuthorizeDto(path[2],token);
        if (method.equals(Method.GET.getVerb())) {
            if (path[3].equals("profile")) {
                return profile(userAuthorizeDto);
            }

            if (path[3].equals("ratings")) {
                return ratings(userAuthorizeDto);
            }

            if (path[3].endsWith("favorites")) {
                return favorites(userAuthorizeDto);
            }
        }

        if (method.equals(Method.PUT.getVerb())) {
            if (path[3].equals("profile")) {
                return update(userAuthorizeDto, body);
            }
        }

        if (method.equals(Method.DELETE.getVerb())) {
            return delete(userAuthorizeDto);
        }

        throw new EntityNotFoundException("Path not found");
    }

    private Response profile(UserAuthorizeDto userAuthorizeDto) {
        UserAuthorizedDto user = this.userService.getUser(userAuthorizeDto);
        return json(user,Status.OK);
    }

    private Response ratings(UserAuthorizeDto userAuthorizeDto) {
        RatingListAuthorizedDto ratings = this.userService.ratings(userAuthorizeDto);
        return json(ratings, Status.OK);
    }

    private Response favorites(UserAuthorizeDto userAuthorizeDto) {
        MediaListAuthorizedDto favorites = this.userService.favorites(userAuthorizeDto);
        return json(favorites, Status.OK);
    }

    private Response update(UserAuthorizeDto userAuthorizeDto, String body) {
        UserUpdateDto userUpdateDto = toObject(body, UserUpdateDto.class);
        UserUpdatedAuthorizedDto user = this.userService.update(userAuthorizeDto, userUpdateDto);
        return json(user,Status.OK);
    }

    private Response delete(UserAuthorizeDto userAuthorizeDto) {
        String username = this.userService.delete(userAuthorizeDto);
        return json(username+" deleted",Status.OK);
    }
}
