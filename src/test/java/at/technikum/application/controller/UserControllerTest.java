package at.technikum.application.controller;

import at.technikum.application.dto.auth.UserCreateDto;
import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.dto.sql.*;
import at.technikum.application.dto.users.UserProfile;
import at.technikum.application.dto.users.UserUpdateDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.User;
import at.technikum.application.service.*;
import at.technikum.server.http.ContentType;
import at.technikum.server.http.Method;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private AuthService authService;
    @Mock
    private RecommendationService recommendationService;
    private TestableUserController userController;

    static class TestableUserController extends UserController {
        TestableUserController(UserService u, AuthService a, RecommendationService r) {
            super(u, a, r);
        }

        @Override
        protected <T> T toOtherObject(RequestDto dto, Class<T> target) {
            return toOtherObjectPublic(dto, target);
        }

        public <T> T toOtherObjectPublic(RequestDto dto, Class<T> target) {
            return super.toOtherObject(dto, target);
        }
    }

    @BeforeEach
    void setUp() {
        userController = spy(new TestableUserController(userService, authService, recommendationService));

    }

    // positive
    // get all users
    @Test
    public void givenGetAllUsers_whenHandle_thenStatusIsOk() {
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"","users"});
        requestDto.setMethod(Method.GET);
        List<SQLUserDto> expectedList = new ArrayList<>();
        expectedList.add(new SQLUserDto());

        when(userService.getUsers()).thenReturn(expectedList);

        Response response = userController.handle(requestDto);

        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(userService, times(1)).getUsers();
    }

    // login
    @Test
    public void givenPostLogin_whenHandle_thenStatusIsAccepted() {
        UserLoginDto userDto = new UserLoginDto();
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"","users", "login"});
        requestDto.setMethod(Method.POST);

        when(authService.createToken(userDto)).thenReturn(new UserLoggedInDto());
        doReturn(userDto).when(userController).toOtherObjectPublic(requestDto, UserLoginDto.class);

        Response response = userController.handle(requestDto);

        assertEquals(Status.ACCEPTED, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(authService, times(1)).createToken(userDto);
    }


    // register
    @Test
    public void givenPostRegister_whenHandle_thenStatusIsCreated() {
        UserCreateDto userDto = new UserCreateDto();
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"","users", "register"});
        requestDto.setMethod(Method.POST);

        when(authService.register(userDto)).thenReturn(new User());
        doReturn(userDto).when(userController).toOtherObjectPublic(requestDto, UserCreateDto.class);

        Response response = userController.handle(requestDto);

        assertEquals(Status.CREATED, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(authService, times(1)).register(userDto);
    }

    // get profile
    @Test
    public void givenGetUsersProfile_whenHandle_thenStatusIsOk() {
        User user = new User();
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "users", "userid","profile"});
        requestDto.setMethod(Method.GET);
        requestDto.setUser(user);

        when(userService.profile(user)).thenReturn(new UserProfile());

        Response response = userController.handle(requestDto);

        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(userService, times(1)).profile(user);
    }

    // get ratings
    @Test
    public void givenGetUsersRatings_whenHandle_thenStatusIsOk() {
        User user = new User();
        List<SQLRatingDto> ratingList = new ArrayList<>();
        ratingList.add(new SQLRatingDto());
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "users", "userid","ratings"});
        requestDto.setMethod(Method.GET);
        requestDto.setUser(user);

        when(userService.ratings(user)).thenReturn(ratingList);

        Response response = userController.handle(requestDto);

        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(userService, times(1)).ratings(user);
    }

    // get favorites
    @Test
    public void givenGetUsersFavorites_whenHandle_thenStatusIsOk() {
        User user = new User();
        List<SQLFavoriteDto> favsList = new ArrayList<>();
        favsList.add(new SQLFavoriteDto());
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "users", "userid","favorites"});
        requestDto.setMethod(Method.GET);
        requestDto.setUser(user);

        when(userService.favorites(user)).thenReturn(favsList);

        Response response = userController.handle(requestDto);

        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(userService, times(1)).favorites(user);
    }

    // get recommendations
    @Test
    public void givenGetUsersRecommendations_whenHandle_thenStatusIsOk() {
        User user = new User();
        List<SQLRecommendationDto> recommList = new ArrayList<>();
        recommList.add(new SQLRecommendationDto());
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "users", "userid","recommendations"});
        requestDto.setMethod(Method.GET);
        requestDto.setUser(user);

        when(recommendationService.getRecommendations(user)).thenReturn(recommList);

        Response response = userController.handle(requestDto);

        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(recommendationService, times(1)).getRecommendations(user);
    }

    // update user, passwords match
    @Test
    public void givenPutProfileAuthorized_whenHandle_thenStatusIsOk() {
        User user = new User();
        user.setPassword("password");
        UserUpdateDto update = new UserUpdateDto();
        update.setPasswordOld(user.getPassword());
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "users", "userid", "profile"});
        requestDto.setMethod(Method.PUT);
        requestDto.setUser(user);

        when(userService.update(user, update)).thenReturn(new UserLoggedInDto());
        doReturn(update).when(userController).toOtherObjectPublic(requestDto, UserUpdateDto.class);

        Response response = userController.handle(requestDto);

        assertNotNull(response);
        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(userService, times(1)).update(user, update);
    }

    // delete user
    @Test
    public void givenDelete_whenHandle_thenStatusIsOk() {
        User user = new User();
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "users", "userid"});
        requestDto.setMethod(Method.DELETE);
        requestDto.setUser(user);

        when(userService.delete(user)).thenReturn("deleted");

        Response response = userController.handle(requestDto);

        assertNotNull(response);
        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.TEXT_PLAIN.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(userService, times(1)).delete(user);
    }

    // negative
    // path not found
    @Test
    public void givenGetId_whenHandle_thenThrowException() {
        User user = new User();
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "users", "userid"});
        requestDto.setMethod(Method.GET);
        requestDto.setUser(user);

        assertThrows(EntityNotFoundException.class, ()-> {
            userController.handle(requestDto);
        });
    }

    // update user, passwords dont match
    @Test
    public void givenPutProfileNotAuthorized_whenHandle_thenStatusIsUnauthorized() {
        User user = new User();
        user.setPassword("password");
        UserUpdateDto update = new UserUpdateDto();
        update.setPasswordOld("otherpassword");
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "users", "userid", "profile"});
        requestDto.setMethod(Method.PUT);
        requestDto.setUser(user);

        doReturn(update).when(userController).toOtherObjectPublic(requestDto, UserUpdateDto.class);

        Response response = userController.handle(requestDto);

        assertNotNull(response);
        assertEquals(Status.UNAUTHORIZED, response.getStatus());
        assertEquals(ContentType.TEXT_PLAIN.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(userService, never()).update(any(), any());
    }

}