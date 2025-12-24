package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.dto.auth.UserCreateDto;
import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.dto.media.MediaListWithTokenDto;
import at.technikum.application.dto.rating.RatingListWithTokenDto;
import at.technikum.application.dto.recommendations.RecommendationsWithTokenDto;
import at.technikum.application.dto.users.*;
import at.technikum.application.enums.MediaType;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.Rating;
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

        if (path.length==2) {
            Response response = new Response();
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody("Users overview");
            return response;
        }

        if (method.equals(Method.POST)) {

            if (path[2].equals("login")) {
                UserLoginDto userLoginDto = toOtherObject(requestDto, UserLoginDto.class);
                return login(userLoginDto);
            }

            if (path[2].equals("register")) {
                UserCreateDto userCreateDto = toOtherObject(requestDto, UserCreateDto.class);
                return createUser(userCreateDto);
            }
        } else {

            User user = requestDto.getUser();
            if (method.equals(Method.GET)) {

                if (path[3].equals("profile")) {
                    return profile(user);
                }

                if (path[3].equals("ratings")) {
                    return ratings(user);
                }

                if (path[3].endsWith("favorites")) {
                    return favorites(user);
                }

                if (path[3].endsWith("recommendations")) {
                    MediaType type = requestDto.getMediaType();
                    return recommendations(user,type);
                }
            }

            if (method.equals(Method.PUT)){
                if (path[3].equals("profile")) {
                    UserUpdateDto userUpdateDto = toOtherObject(requestDto, UserUpdateDto.class);
                    userUpdateDto.setId(user.getId());
                    return update(user,userUpdateDto);
                }
            }

            if (method.equals(Method.DELETE)) {
                return delete(user);
            }
        }

        throw new EntityNotFoundException("Path not found");
    }

    private Response recommendations(User user, MediaType mediaType) {
        List<Media> recommendations = this.recommendationService.getRecommendations(
                user,mediaType
        );
        return json(recommendations,Status.OK);
    }

    private Response profile(User user) {
        return json(user,Status.OK);
    }

    private Response ratings(User user) {
        List<Rating> ratings = this.userService.ratings(user);
        return json(ratings, Status.OK);
    }

    private Response favorites(User user) {
        List<Media> favorites = this.userService.favorites(user);
        return json(favorites, Status.OK);
    }

    private Response update(User user, UserUpdateDto userUpdateDto) {
        UserLoggedInDto userUpdated = this.userService.update(user, userUpdateDto);
        return json(userUpdated,Status.OK);
    }

    private Response delete(User user) {
        String username = this.userService.delete(user);
        return text(username+" deleted",Status.OK);
    }

    private Response login(UserLoginDto userLoginDto) {
        UserLoggedInDto userLoggedInDto = this.authService.createToken(userLoginDto);
        return json(userLoggedInDto, Status.ACCEPTED);
    }

    private Response createUser(UserCreateDto userCreateDto) {
        User user = this.authService.register(userCreateDto);
        return json(user, Status.CREATED);
    }
}
