package at.technikum.application.controller;

import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.dto.sql.SQLLikeDto;
import at.technikum.application.dto.sql.SQLRatingDto;
import at.technikum.application.enums.Stars;
import at.technikum.application.enums.UserType;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.NotAuthorizedException;
import at.technikum.application.model.*;
import at.technikum.application.service.RatingService;
import at.technikum.server.http.ContentType;
import at.technikum.server.http.Method;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class RatingControllerTest {

    @Mock
    private RatingService ratingService;
    private TestableRatingController ratingController;

    static class TestableRatingController extends RatingController {
        TestableRatingController(RatingService r) {
            super(r);
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
        ratingController = spy(new TestableRatingController(ratingService));

    }

    // positive
    // get all ratings
    @Test
    public void givenGetAllRatings_whenHandle_thenStatusIsOk() {
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"","ratings"});
        requestDto.setMethod(Method.GET);
        List<SQLRatingDto> expectedList = new ArrayList<>();
        expectedList.add(new SQLRatingDto());

        when(ratingService.allRatings()).thenReturn(expectedList);

        Response response = ratingController.handle(requestDto);

        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(ratingService, times(1)).allRatings();
    }

    // update rating, user and creator match
    @Test
    public void givenPutRatingAuthorized_whenHandle_thenStatusIsOk() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        UUID ratingId = UUID.randomUUID();
        Rating rating = new Rating();
        rating.setCreator(user);
        Rating update = new Rating();
        SQLRatingDto updatedDto = new SQLRatingDto();
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "ratings", ratingId.toString()});
        requestDto.setMethod(Method.PUT);
        requestDto.setUser(user);
        requestDto.setStars(3);

        when(ratingService.findById(ratingId)).thenReturn(rating);
        when(ratingService.update(rating, update)).thenReturn(updatedDto);
        doReturn(update).when(ratingController).toOtherObjectPublic(requestDto, Rating.class);

        Response response = ratingController.handle(requestDto);

        assertNotNull(response);
        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(ratingService, times(1)).findById(ratingId);

        ArgumentCaptor<Rating> ratingCaptor = ArgumentCaptor.forClass(Rating.class);
        verify(ratingService, times(1)).update(eq(rating), ratingCaptor.capture());
        // eq(), damit beide Argumente matcher sind

        Rating capturedRating = ratingCaptor.getValue();
        assertEquals(Stars.THREE, capturedRating.getStars());
    }

    // delete rating
    @Test
    public void givenDeleteRatingAuthorized_whenHandle_thenStatusIsOk() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        UUID ratingId = UUID.randomUUID();
        Rating rating = new Rating();
        rating.setCreator(user);
        String deleted = "deleted";
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "ratings", ratingId.toString()});
        requestDto.setMethod(Method.DELETE);
        requestDto.setUser(user);

        when(ratingService.findById(ratingId)).thenReturn(rating);
        when(ratingService.delete(rating)).thenReturn(deleted);

        Response response = ratingController.handle(requestDto);

        assertNotNull(response);
        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.TEXT_PLAIN.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(ratingService, times(1)).findById(ratingId);
        verify(ratingService, times(1)).delete(rating);
    }

    // create like
    @Test
    public void givenPostLikeAuthorized_whenHandle_thenStatusIsCreated() {
        Rating rating = new Rating();
        UUID ratingId = UUID.randomUUID();
        rating.setId(ratingId);
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        User creator = new User();
        creator.setId(UUID.randomUUID());
        rating.setCreator(creator);
        SQLLikeDto createdDto = new SQLLikeDto();
        RequestDto requestDto = new RequestDto();
        requestDto.setMethod(Method.POST);
        requestDto.setUser(user);
        requestDto.setPath(new String[] {"", "ratings", ratingId.toString(), "like"});

        when(ratingService.findById(ratingId)).thenReturn(rating);
        when(ratingService.like(any(Like.class))).thenReturn(createdDto);

        Response response = ratingController.handle(requestDto);

        assertEquals(Status.CREATED, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(ratingService, times(1)).findById(ratingId);

        ArgumentCaptor<Like> likeCaptor = ArgumentCaptor.forClass(Like.class);
        verify(ratingService, times(1)).like(likeCaptor.capture());

        Like capturedLike = likeCaptor.getValue();
        assertNotNull(capturedLike);
        assertEquals(rating, capturedLike.getRating());
        assertEquals(user, capturedLike.getUser());
    }

    // confirm rating
    @Test
    public void givenPostConfirmAuthorized_whenHandle_thenStatusIsCreated() {
        Rating rating = new Rating();
        UUID ratingId = UUID.randomUUID();
        rating.setId(ratingId);
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setUserType(UserType.Admin);
        SQLRatingDto createdDto = new SQLRatingDto();
        RequestDto requestDto = new RequestDto();
        requestDto.setMethod(Method.POST);
        requestDto.setUser(user);
        requestDto.setPath(new String[] {"", "ratings", ratingId.toString(), "confirm"});

        when(ratingService.findById(ratingId)).thenReturn(rating);
        when(ratingService.confirm(rating)).thenReturn(createdDto);

        Response response = ratingController.handle(requestDto);

        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(ratingService, times(1)).findById(ratingId);
        verify(ratingService, times(1)).confirm(rating);
    }

    // negative
    // path not found
    @Test
    public void givenPostId_whenHandle_thenThrowException() {
        UUID Id = UUID.randomUUID();
        User user = new User();
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "ratings", Id.toString()});
        requestDto.setMethod(Method.POST);
        requestDto.setUser(user);

        when(ratingService.findById(Id)).thenReturn(new Rating());

        assertThrows(EntityNotFoundException.class, ()-> {
            ratingController.handle(requestDto);
        });
    }

    // update rating, user and creator dont match
    @Test
    public void givenPutMediaNotAuthorized_whenHandle_thenthrowException() {
        UUID ratingId = UUID.randomUUID();
        Rating rating = new Rating();
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        User ratingCreator = new User();
        ratingCreator.setId(UUID.randomUUID());
        rating.setCreator(ratingCreator);
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "ratings", ratingId.toString()});
        requestDto.setMethod(Method.PUT);
        requestDto.setUser(user);

        when(ratingService.findById(ratingId)).thenReturn(rating);

        assertThrows(NotAuthorizedException.class, ()-> {
            ratingController.handle(requestDto);
        });
        verify(ratingService, never()).update(any(), any());
    }

    // delete rating, user and creator dont match
    @Test
    public void givenDeleteRatingNotAuthorized_whenHandle_thenthrowException() {
        UUID ratingId = UUID.randomUUID();
        Rating rating = new Rating();
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        User ratingCreator = new User();
        ratingCreator.setId(UUID.randomUUID());
        rating.setCreator(ratingCreator);
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "ratings", ratingId.toString()});
        requestDto.setMethod(Method.DELETE);
        requestDto.setUser(user);

        when(ratingService.findById(ratingId)).thenReturn(rating);

        assertThrows(NotAuthorizedException.class, ()-> {
            ratingController.handle(requestDto);
        });
        verify(ratingService, never()).delete(any());
    }

    // create like, user and creator match
    @Test
    public void givenPostLikeNotAuthorized_whenHandle_thenthrowExepction() {
        Rating rating = new Rating();
        UUID ratingId = UUID.randomUUID();
        rating.setId(ratingId);
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        rating.setCreator(user);
        RequestDto requestDto = new RequestDto();
        requestDto.setMethod(Method.POST);
        requestDto.setUser(user);
        requestDto.setPath(new String[] {"", "ratings", ratingId.toString(), "like"});

        when(ratingService.findById(ratingId)).thenReturn(rating);

        assertThrows(NotAuthorizedException.class, ()-> {
            ratingController.handle(requestDto);
        });
        verify(ratingService, never()).like(any());
    }

    // confirm rating, no admin
    @Test
    public void givenPostConfirmNotAuthorized_whenHandle_thenStatusIsCreated() {
        Rating rating = new Rating();
        UUID ratingId = UUID.randomUUID();
        rating.setId(ratingId);
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setUserType(UserType.User);
        RequestDto requestDto = new RequestDto();
        requestDto.setMethod(Method.POST);
        requestDto.setUser(user);
        requestDto.setPath(new String[] {"", "ratings", ratingId.toString(), "confirm"});

        when(ratingService.findById(ratingId)).thenReturn(rating);

        assertThrows(NotAuthorizedException.class, ()-> {
            ratingController.handle(requestDto);
        });
        verify(ratingService, never()).confirm(any());
    }
}