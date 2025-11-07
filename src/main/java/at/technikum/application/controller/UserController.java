package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.dto.auth.UserCreateDto;
import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.dto.media.MediaListAuthorizedDto;
import at.technikum.application.dto.rating.RatingListAuthorizedDto;
import at.technikum.application.dto.recommendations.RecommendationsAuthorizedDto;
import at.technikum.application.dto.users.*;
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

            String id = requestDto.getId();
            String token = requestDto.getToken();
            if (method.equals(Method.GET)) {

                if (path[3].equals("profile")) {
                    return profile(id,token);
                }

                if (path[3].equals("ratings")) {
                    return ratings(id,token);
                }

                if (path[3].endsWith("favorites")) {
                    return favorites(id,token);
                }

                if (path[3].endsWith("recommendations")) {
                    ContentType type = requestDto.getContentType();
                    return recommendations(id,type,token);
                }
            }

            if (method.equals(Method.PUT)){
                if (path[3].equals("profile")) {
                    UserUpdateDto userUpdateDto = toOtherObject(requestDto, UserUpdateDto.class);
                    return update(userUpdateDto,token);
                }
            }

            if (method.equals(Method.DELETE)) {
                return delete(id);
            }
        }

        throw new EntityNotFoundException("Path not found");
    }

    private Response recommendations(String id, ContentType contentType, String token) {
        List<Media> recommendations = this.recommendationService.getRecommendations(
                id,contentType
        );
        RecommendationsAuthorizedDto recomAuth = new RecommendationsAuthorizedDto(recommendations,token);
        return json(recomAuth,Status.OK);
    }

    private Response profile(String id,String token) {
        User user = this.userService.getUser(id);
        UserAuthorizedDto userAuthorized = new UserAuthorizedDto(user,token);
        return json(userAuthorized,Status.OK);
    }

    private Response ratings(String id,String token) {
        List<Rating> ratings = this.userService.ratings(id);
        RatingListAuthorizedDto ratingsAuth = new RatingListAuthorizedDto(token,ratings);
        return json(ratingsAuth, Status.OK);
    }

    private Response favorites(String id, String token) {
        List<Media> favorites = this.userService.favorites(id);
        MediaListAuthorizedDto favsAuth = new MediaListAuthorizedDto(token,favorites);
        return json(favsAuth, Status.OK);
    }

    private Response update(UserUpdateDto userUpdateDto,String token) {
        UserUpdatedDto userUpdated = this.userService.update(userUpdateDto);
        UserUpdatedAuthorizedDto updatedAuth = new UserUpdatedAuthorizedDto(token, userUpdated);
        return json(updatedAuth,Status.OK);
    }

    private Response delete(String id) {
        String username = this.userService.delete(id);
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
