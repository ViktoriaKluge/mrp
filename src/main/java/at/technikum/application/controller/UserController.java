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
            String token = requestDto.getToken();
            if (method.equals(Method.GET)) {

                if (path[3].equals("profile")) {
                    return profile(user,token);
                }

                if (path[3].equals("ratings")) {
                    return ratings(user,token);
                }

                if (path[3].endsWith("favorites")) {
                    return favorites(user,token);
                }

                if (path[3].endsWith("recommendations")) {
                    ContentType type = requestDto.getContentType();
                    return recommendations(user,type,token);
                }
            }

            if (method.equals(Method.PUT)){
                if (path[3].equals("profile")) {
                    UserUpdateDto userUpdateDto = toOtherObject(requestDto, UserUpdateDto.class);
                    userUpdateDto.setId(user.getId());
                    return update(user,userUpdateDto,token);
                }
            }

            if (method.equals(Method.DELETE)) {
                return delete(user);
            }
        }

        throw new EntityNotFoundException("Path not found");
    }

    private Response recommendations(User user, ContentType contentType, String token) {
        List<Media> recommendations = this.recommendationService.getRecommendations(
                user,contentType
        );
        RecommendationsWithTokenDto recomWToken = new RecommendationsWithTokenDto(recommendations,token);
        return json(recomWToken,Status.OK);
    }

    private Response profile(User user,String token) {
        UserWithTokenDto userWToken = new UserWithTokenDto(user,token);
        return json(userWToken,Status.OK);
    }

    private Response ratings(User user,String token) {
        List<Rating> ratings = this.userService.ratings(user.getId());
        RatingListWithTokenDto ratingsWToken = new RatingListWithTokenDto(token,ratings);
        return json(ratingsWToken, Status.OK);
    }

    private Response favorites(User user, String token) {
        List<Media> favorites = this.userService.favorites(user.getId());
        MediaListWithTokenDto favsWToken = new MediaListWithTokenDto(token,favorites);
        return json(favsWToken, Status.OK);
    }

    private Response update(User user, UserUpdateDto userUpdateDto,String token) {
        UserLoggedInDto userUpdated = this.userService.update(user, userUpdateDto);
        // UserUpdatedWithTokenDto updatedWToken = new UserUpdatedWithTokenDto(token, userUpdated);
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
