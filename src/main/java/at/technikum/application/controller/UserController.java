package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.dto.auth.UserCreateDto;
import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.dto.sql.*;
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

        if (method.equals(Method.GET) && path.length==2) {
            List<SQLUserDto> userList = this.userService.getUsers();
            return json(userList, Status.OK);
        } else if (method.equals(Method.POST) &&  path.length==3) {
            if (path[2].equals("login")) {
                UserLoginDto userLoginDto = toOtherObject(requestDto, UserLoginDto.class);
                return login(userLoginDto);
            } else if (path[2].equals("register")) {
                UserCreateDto userCreateDto = toOtherObject(requestDto, UserCreateDto.class);
                return createUser(userCreateDto);
            }
        } else {
            User user = requestDto.getUser();
            if (path.length==4) {
                if (method.equals(Method.GET)) {
                    if (path[3].equals("profile")) {
                        return profile(user);
                    } else if (path[3].equals("ratings")) {
                        return ratings(user);
                    } else if (path[3].equals("favorites")) {
                        return favorites(user);
                    } else if (path[3].equals("recommendations")) {
                        return recommendations(user);
                    }
                } else if (method.equals(Method.PUT)){
                    if (path[3].equals("profile")) {
                        UserUpdateDto userUpdateDto = toOtherObject(requestDto, UserUpdateDto.class);
                        return update(user,userUpdateDto);
                    }
                }
            } else if (method.equals(Method.DELETE) && path.length==3) {
                return delete(user);
            }
        }
        throw new EntityNotFoundException("Path not found");
    }

    private Response recommendations(User user) {
        List<SQLRecommendationDto> recommendations = this.recommendationService.getRecommendations(user);
        return json(recommendations,Status.OK);
    }

    private Response profile(User user) {
        UserProfile profile = this.userService.profile(user);
        return json(profile,Status.OK);
    }

    private Response ratings(User user) {
        List<SQLRatingDto> ratings = this.userService.ratings(user);
        return json(ratings, Status.OK);
    }

    private Response favorites(User user) {
        List<SQLFavoriteDto> favorites = this.userService.favorites(user);
        return json(favorites, Status.OK);
    }

    private Response update(User user, UserUpdateDto userUpdate) {
        if (user.getPassword().equals(userUpdate.getPasswordOld())) {
            UserLoggedInDto userUpdated = this.userService.update(user,userUpdate);
            return json(userUpdated,Status.OK);
        } else {
            return text("Passwords do not match", Status.UNAUTHORIZED);
        }
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
