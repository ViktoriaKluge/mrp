package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.dto.auth.UserCreateDto;
import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.dto.media.MediaListAuthorizedDto;
import at.technikum.application.dto.rating.RatingListAuthorizedDto;
import at.technikum.application.dto.users.*;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.User;
import at.technikum.application.model.Media;
import at.technikum.application.service.AuthService;
import at.technikum.application.service.RecommendationService;
import at.technikum.application.service.UserService;
import at.technikum.server.http.*;

import java.util.List;

public class UserController extends Controller {

    private final UserService userService;
    private final AuthService authService;
    private final RecommendationService recommendationService;

    public UserController(UserService userService, AuthService authService,
                          RecommendationService recommendationService) {

        this.userService = userService;
        this.authService = authService;
        this.recommendationService = recommendationService;
    }

    @Override
    public Response handle(RequestDto requestDto) {
        String[] path = requestDto.getPath();
        Method method = requestDto.getMethod();

        // request to user?

        if (path.length==1) {
            Response response = new Response();
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody("Users overview");
            return response;
        }

        if (method.equals(Method.POST)) {

            if (path[2].equals("login")) {
                return login("body");
            }

            if (path[2].equals("register")) {
                return createUser("body");
            }
        } else {

            if (method.equals(Method.GET)) {
                if (path[3].equals("profile")) {
                    return profile(requestDto);
                }

                if (path[3].equals("ratings")) {
                    return ratings(requestDto);
                }

                if (path[3].endsWith("favorites")) {
                    return favorites(requestDto);
                }

                if (path[3].endsWith("recommendations")) {
                    return recommendations(requestDto);
                }
            }

            if (method.equals(Method.PUT.getVerb())) {
                if (path[3].equals("profile")) {
                    return update(requestDto);
                }
            }

            if (method.equals(Method.DELETE.getVerb())) {
                return delete(requestDto);
            }
        }

        throw new EntityNotFoundException("Path not found");
    }

    private Response recommendations(RequestDto requestDto) {
        List<Media> recommendations = this.recommendationService.getRecommendations(
                userAuthorizeDto,body
        );
        return json(recommendations,Status.OK);
    }

    private Response profile(RequestDto requestDto) {
        UserAuthorizedDto user = this.userService.getUser(userAuthorizeDto);
        return json(user,Status.OK);
    }

    private Response ratings(RequestDto requestDto) {
        RatingListAuthorizedDto ratings = this.userService.ratings(userAuthorizeDto);
        return json(ratings, Status.OK);
    }

    private Response favorites(RequestDto requestDto) {
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
        return text(username+" deleted",Status.OK);
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
