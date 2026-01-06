package at.technikum.application.service;

import at.technikum.application.dto.sql.SQLMediaDto;
import at.technikum.application.dto.users.UserUpdateDto;
import at.technikum.application.enums.MediaType;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.Media;
import at.technikum.application.repository.MediaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MediaServiceTest {

    @Mock
    private MediaRepository mediaRepository;
    @InjectMocks
    private MediaService mediaService;

    // positive
    // update verwendet alte daten
    @Test
    public void givenUpdateAlmostEmpty_whenMediaUpdate_thenUsesOldData() {
        Media old  = new Media();
        old.setTitle("title");
        old.setDescription("description");
        old.setMediaType(MediaType.GAME);
        old.setReleaseYear(2018);
        old.setAgeRestriction(14);
        old.setGenre(List.of("Drama", "Horror"));
        Media update = new Media();
        update.setTitle("newTitle");

        when(mediaRepository.update(any(Media.class))).thenReturn(Optional.of(new SQLMediaDto()));

        SQLMediaDto result = mediaService.update(old, update);

        assertNotNull(result);
        ArgumentCaptor<Media> captor = ArgumentCaptor.forClass(Media.class);
        verify(mediaRepository, times(1)).update(captor.capture());

        Media sentToRepo = captor.getValue();

        assertNotNull(sentToRepo);
        assertEquals(update.getTitle(), sentToRepo.getTitle());
        assertEquals(old.getDescription(), sentToRepo.getDescription());
        assertEquals(old.getMediaType(), sentToRepo.getMediaType());
        assertEquals(old.getReleaseYear(), sentToRepo.getReleaseYear());
        assertEquals(old.getAgeRestriction(), sentToRepo.getAgeRestriction());
        assertEquals(old.getGenre(), sentToRepo.getGenre());

    }

    // update behält neue daten
    @Test
    public void givenUpdateAll_whenMediaUpdate_thenKeepNewData() {
        Media old  = new Media();
        old.setTitle("title");
        old.setDescription("description");
        old.setMediaType(MediaType.GAME);
        old.setReleaseYear(2018);
        old.setAgeRestriction(14);
        old.setGenre(List.of("Drama", "Horror"));
        Media update = new Media();
        update.setTitle("newTitle");
        update.setDescription("newDescription");
        update.setMediaType(MediaType.MOVIE);
        update.setReleaseYear(2020);
        update.setAgeRestriction(7);
        update.setGenre(List.of("Comedy"));

        when(mediaRepository.update(any(Media.class))).thenReturn(Optional.of(new SQLMediaDto()));

        SQLMediaDto result = mediaService.update(old, update);

        assertNotNull(result);
        ArgumentCaptor<Media> captor = ArgumentCaptor.forClass(Media.class);
        verify(mediaRepository, times(1)).update(captor.capture());

        Media sentToRepo = captor.getValue();

        assertNotNull(sentToRepo);
        assertEquals(update.getTitle(), sentToRepo.getTitle());
        assertEquals(update.getDescription(), sentToRepo.getDescription());
        assertEquals(update.getMediaType(), sentToRepo.getMediaType());
        assertEquals(update.getReleaseYear(), sentToRepo.getReleaseYear());
        assertEquals(update.getAgeRestriction(), sentToRepo.getAgeRestriction());
        assertEquals(update.getGenre(), sentToRepo.getGenre());
    }

    // negative
    // update optional empty
    @Test
    public void givenOptionalEmpty_whenMediaUpdate_thenThrowException() {
        Media old = new Media();
        Media update = new Media();

        when(mediaRepository.update(any(Media.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> mediaService.update(old, update));
    }


}