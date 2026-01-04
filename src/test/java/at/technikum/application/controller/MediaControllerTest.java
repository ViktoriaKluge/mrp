package at.technikum.application.controller;

import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.dto.sql.SQLMediaDto;
import at.technikum.application.dto.users.UserUpdateDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.NotAuthorizedException;
import at.technikum.application.model.Media;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @Test
    public void givenPathMediaIdMethodPutUserIsCreator_whenHandle_thenStatusIsOk() {
        RequestDto requestDto = new RequestDto();
        UUID mediaId = UUID.fromString("ef3e67e1-9a5e-49ce-a2a7-95aa2209b295");
        Media expectedMedia = new Media();
        requestDto.setPath(new String[] {"", "media", mediaId.toString()});
        requestDto.setMethod(Method.PUT);
        UUID userId = UUID.fromString("ff3e67e1-9a5e-49ce-a2a7-95aa2209b295");
        User expectedUser = new User();
        expectedUser.setId(userId);
        requestDto.setUser(expectedUser);
        expectedMedia.setCreator(expectedUser);
        Media update = new Media();
        SQLMediaDto updatedDto = new SQLMediaDto();

        when(mediaService.findById(mediaId)).thenReturn(expectedMedia);
        doReturn(update).when(mediaController).toOtherObjectPublic(requestDto, Media.class);
        when(mediaService.update(expectedMedia, update)).thenReturn(updatedDto);

        Response response = mediaController.handle(requestDto);

        assertNotNull(response);
        assertEquals(Status.OK, response.getStatus());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
        assertNotNull(response.getBody());
        verify(mediaService, times(1)).findById(mediaId);
        verify(mediaService, times(1)).update(expectedMedia, update);

    }


    // negative
    @Test
    public void givenPathMediaIdMethodPost_whenHandle_thenThrowException() {
        RequestDto requestDto = new RequestDto();
        UUID mockId = UUID.fromString("ef3e67e1-9a5e-49ce-a2a7-95aa2209b295");
        requestDto.setPath(new String[] {"", "media", mockId.toString()});
        requestDto.setMethod(Method.POST);

        when(mediaService.findById(mockId)).thenReturn(new Media());

        assertThrows(EntityNotFoundException.class, ()-> {
            mediaController.handle(requestDto);
        });
    }

    @Test
    public void givenPathMediaIdMethodPutUserIsNotCreator_whenHandle_thenthrowException() {
        RequestDto requestDto = new RequestDto();
        UUID mediaId = UUID.fromString("ef3e67e1-9a5e-49ce-a2a7-95aa2209b295");
        Media expectedMedia = new Media();
        requestDto.setPath(new String[] {"", "media", mediaId.toString()});
        requestDto.setMethod(Method.PUT);
        UUID userId = UUID.fromString("ff3e67e1-9a5e-49ce-a2a7-95aa2209b295");
        User expectedUser = new User();
        expectedUser.setId(userId);
        User mediaCreator = new User();
        mediaCreator.setId(mediaId);
        requestDto.setUser(expectedUser);
        expectedMedia.setCreator(mediaCreator);

        when(mediaService.findById(mediaId)).thenReturn(expectedMedia);

        assertThrows(NotAuthorizedException.class, ()-> {
            mediaController.handle(requestDto);
        });
        verify(mediaService, never()).update(any(), any());
    }

}