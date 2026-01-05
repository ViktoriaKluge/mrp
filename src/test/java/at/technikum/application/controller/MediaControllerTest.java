package at.technikum.application.controller;

import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.dto.media.MediaProfile;
import at.technikum.application.dto.sql.SQLFavoriteDto;
import at.technikum.application.dto.sql.SQLMediaDto;
import at.technikum.application.dto.sql.SQLRatingDto;
import at.technikum.application.enums.Stars;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.NotAuthorizedException;
import at.technikum.application.model.Favorite;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;
import at.technikum.application.service.FavoritesService;
import at.technikum.application.service.MediaService;
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

@ExtendWith(MockitoExtension.class)
class MediaControllerTest {

    @Mock
    private MediaService mediaService;
    @Mock
    private FavoritesService favoritesService;
    @Mock
    private RatingService ratingService;
    private TestableMediaController mediaController;


    static class TestableMediaController extends MediaController {
        TestableMediaController(MediaService m, FavoritesService f, RatingService r) {
            super(m, f, r);
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
        mediaController = spy(new TestableMediaController(mediaService, favoritesService, ratingService));

    }

    // positive
    // get all media
    @Test
    public void givenGetAllMedia_whenHandle_thenStatusIsOk() {
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"","media"});
        requestDto.setMethod(Method.GET);
        List<SQLMediaDto> expectedList = new ArrayList<>();
        expectedList.add(new SQLMediaDto());

        when(mediaService.findAll()).thenReturn(expectedList);

        Response response = mediaController.handle(requestDto);

        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(mediaService, times(1)).findAll();
    }

    // create media
    @Test
    public void givenPostMedia_whenHandle_thenStatusIsCreated() {
        User user = new User();
        Media media = new Media();
        SQLMediaDto createdDto = new SQLMediaDto();
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"","media"});
        requestDto.setMethod(Method.POST);
        requestDto.setUser(user);

        when(mediaService.create(any(Media.class))).thenReturn(createdDto);
        doReturn(media).when(mediaController).toOtherObjectPublic(requestDto, Media.class);

        Response response = mediaController.handle(requestDto);

        assertEquals(Status.CREATED, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());

        ArgumentCaptor<Media> mediaCaptor = ArgumentCaptor.forClass(Media.class);
        verify(mediaService, times(1)).create(mediaCaptor.capture());

        Media capturedMedia = mediaCaptor.getValue();
        assertNotNull(capturedMedia);
        assertEquals(user, capturedMedia.getCreator());
        assertNotNull(capturedMedia.getId());
    }

    // get media profile
    @Test
    public void givenGetMediaWithId_whenHandle_thenStatusIsOk() {
        Media media = new Media();
        MediaProfile profile = new MediaProfile();
        UUID mediaId = UUID.randomUUID();
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "media", mediaId.toString()});
        requestDto.setMethod(Method.GET);

        when(mediaService.findById(mediaId)).thenReturn(media);
        when(mediaService.profile(media)).thenReturn(profile);

        Response response = mediaController.handle(requestDto);

        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(mediaService, times(1)).findById(mediaId);
        verify(mediaService, times(1)).profile(media);
    }

    // create rating
    @Test
    public void givenPostRating_whenHandle_thenStatusIsCreated() {
        Media media = new Media();
        UUID mediaId = UUID.randomUUID();
        media.setId(mediaId);
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        SQLRatingDto createdDto = new SQLRatingDto();
        Rating mappedRating = new Rating();

        RequestDto requestDto = new RequestDto();
        requestDto.setStars(3);
        requestDto.setMethod(Method.POST);
        requestDto.setUser(user);
        requestDto.setPath(new String[] {"", "media", mediaId.toString(), "rate"});

        when(mediaService.findById(mediaId)).thenReturn(media);
        when(ratingService.create(any(Rating.class))).thenReturn(createdDto);
        doReturn(mappedRating).when(mediaController).toOtherObjectPublic(requestDto, Rating.class);

        Response response = mediaController.handle(requestDto);

        assertEquals(Status.CREATED, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(mediaService, times(1)).findById(mediaId);

        ArgumentCaptor<Rating> ratingCaptor = ArgumentCaptor.forClass(Rating.class);
        verify(ratingService, times(1)).create(ratingCaptor.capture());

        Rating capturedRating = ratingCaptor.getValue();
        assertNotNull(capturedRating);
        assertNotNull(capturedRating.getId());
        assertEquals(media, capturedRating.getRatedMedia());
        assertEquals(user, capturedRating.getCreator());
        assertEquals(Stars.THREE, capturedRating.getStars());
    }

    // create favorite
    @Test
    public void givenPostFavorite_whenHandle_thenStatusIsCreated() {
        Media media = new Media();
        UUID mediaId = UUID.randomUUID();
        media.setId(mediaId);
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        SQLFavoriteDto createdDto = new SQLFavoriteDto();
        RequestDto requestDto = new RequestDto();
        requestDto.setMethod(Method.POST);
        requestDto.setUser(user);
        requestDto.setPath(new String[] {"", "media", mediaId.toString(), "favorite"});

        when(mediaService.findById(mediaId)).thenReturn(media);
        when(favoritesService.add(any(Favorite.class))).thenReturn(createdDto);

        Response response = mediaController.handle(requestDto);

        assertEquals(Status.CREATED, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(mediaService, times(1)).findById(mediaId);

        ArgumentCaptor<Favorite> favoriteCaptor = ArgumentCaptor.forClass(Favorite.class);
        verify(favoritesService, times(1)).add(favoriteCaptor.capture());

        Favorite capturedFavorite = favoriteCaptor.getValue();
        assertNotNull(capturedFavorite);
        assertEquals(media, capturedFavorite.getMedia());
        assertEquals(user, capturedFavorite.getUser());
    }

    // update media, user and creator match
    @Test
    public void givenPutMediaAuthorized_whenHandle_thenStatusIsOk() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        UUID mediaId = UUID.randomUUID();
        Media media = new Media();
        media.setCreator(user);
        Media update = new Media();
        SQLMediaDto updatedDto = new SQLMediaDto();
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "media", mediaId.toString()});
        requestDto.setMethod(Method.PUT);
        requestDto.setUser(user);

        when(mediaService.findById(mediaId)).thenReturn(media);
        when(mediaService.update(media, update)).thenReturn(updatedDto);
        doReturn(update).when(mediaController).toOtherObjectPublic(requestDto, Media.class);

        Response response = mediaController.handle(requestDto);

        assertNotNull(response);
        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(mediaService, times(1)).findById(mediaId);
        verify(mediaService, times(1)).update(media, update);
    }

    // delete media
    @Test
    public void givenDeleteMediaAuthorized_whenHandle_thenStatusIsOk() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        UUID mediaId = UUID.randomUUID();
        Media media = new Media();
        media.setCreator(user);
        String deleted = "deleted";
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "media", mediaId.toString()});
        requestDto.setMethod(Method.DELETE);
        requestDto.setUser(user);

        when(mediaService.findById(mediaId)).thenReturn(media);
        when(mediaService.delete(media)).thenReturn(deleted);

        Response response = mediaController.handle(requestDto);

        assertNotNull(response);
        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.TEXT_PLAIN.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(mediaService, times(1)).findById(mediaId);
        verify(mediaService, times(1)).delete(media);
    }

    // delete favorite
    @Test
    public void givenDeleteFavorite_whenHandle_thenStatusIsOK() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        Media media = new Media();
        UUID mediaId = UUID.randomUUID();
        media.setId(mediaId);
        media.setCreator(user);
        String deleted = "deleted";
        RequestDto requestDto = new RequestDto();
        requestDto.setMethod(Method.DELETE);
        requestDto.setUser(user);
        requestDto.setPath(new String[] {"", "media", mediaId.toString(), "favorite"});

        when(mediaService.findById(mediaId)).thenReturn(media);
        when(favoritesService.delete(any(Favorite.class))).thenReturn(deleted);

        Response response = mediaController.handle(requestDto);

        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.TEXT_PLAIN.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(mediaService, times(1)).findById(mediaId);

        ArgumentCaptor<Favorite> favoriteCaptor = ArgumentCaptor.forClass(Favorite.class);
        verify(favoritesService, times(1)).delete(favoriteCaptor.capture());

        Favorite capturedFavorite = favoriteCaptor.getValue();
        assertNotNull(capturedFavorite);
        assertEquals(media, capturedFavorite.getMedia());
        assertEquals(user, capturedFavorite.getUser());
    }

    // negative
    // path not found
    @Test
    public void givenPostId_whenHandle_thenThrowException() {
        UUID Id = UUID.randomUUID();
        User user = new User();
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "media", Id.toString()});
        requestDto.setMethod(Method.POST);
        requestDto.setUser(user);

        when(mediaService.findById(Id)).thenReturn(new Media());

        assertThrows(EntityNotFoundException.class, ()-> {
            mediaController.handle(requestDto);
        });
    }

    // update media, user and creator dont match
    @Test
    public void givenPutMediaNotAuthorized_whenHandle_thenthrowException() {
        UUID mediaId = UUID.randomUUID();
        Media media = new Media();
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        User mediaCreator = new User();
        mediaCreator.setId(UUID.randomUUID());
        media.setCreator(mediaCreator);
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "media", mediaId.toString()});
        requestDto.setMethod(Method.PUT);
        requestDto.setUser(user);

        when(mediaService.findById(mediaId)).thenReturn(media);

        assertThrows(NotAuthorizedException.class, ()-> {
            mediaController.handle(requestDto);
        });
        verify(mediaService, never()).update(any(), any());
    }

    // delete media, user and creator dont match
    @Test
    public void givenDeleteMediaNotAuthorized_whenHandle_thenthrowException() {
        UUID mediaId = UUID.randomUUID();
        Media media = new Media();
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        User mediaCreator = new User();
        mediaCreator.setId(UUID.randomUUID());
        media.setCreator(mediaCreator);
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "media", mediaId.toString()});
        requestDto.setMethod(Method.DELETE);
        requestDto.setUser(user);

        when(mediaService.findById(mediaId)).thenReturn(media);

        assertThrows(NotAuthorizedException.class, ()-> {
            mediaController.handle(requestDto);
        });
        verify(mediaService, never()).delete(any());
    }

}