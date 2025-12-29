package at.technikum.application.service;

import at.technikum.application.dto.sql.SQLMediaDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.UnprocessableEntityException;
import at.technikum.application.model.Media;
import at.technikum.application.repository.MediaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MediaService {

    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public List<SQLMediaDto> findAll() {
        return this.mediaRepository.mediaList();
    }

    public Media findById(UUID id) {
        Optional<Media> media = this.mediaRepository.findByIdMedia(id);
        if (media.isEmpty()) {
            notFound();
        }
        return media.get();
    }

    public SQLMediaDto create(Media media) {
        Optional<SQLMediaDto> createdMedia = this.mediaRepository.save(media);
        if (createdMedia.isEmpty()) {
            throw new UnprocessableEntityException("Media already exists");
        }
        return createdMedia.get();
    }

    public SQLMediaDto update(Media old, Media mediaUpdate) {
        mediaUpdate = setUpdate(old, mediaUpdate);
        Optional<SQLMediaDto> updated = this.mediaRepository.update(mediaUpdate);
        if (updated.isEmpty()) {
            notFound();
        }
        return updated.get();
    }

    public String delete(Media media) {
        Optional<String> deleted = this.mediaRepository.delete(media);
        if (deleted.isEmpty()) {
            notFound();
        }
        return deleted.get();
    }

    public SQLMediaDto convert(Media media) {
        SQLMediaDto sqlMedia = new SQLMediaDto();

        sqlMedia.setId(media.getId());
        sqlMedia.setCreatorId(media.getCreator().getId());
        sqlMedia.setTitle(media.getTitle());
        sqlMedia.setDescription(media.getDescription());
        sqlMedia.setMediaType(media.getMediaType());
        sqlMedia.setReleaseYear(media.getReleaseYear());
        sqlMedia.setAgeRestriction(media.getAgeRestriction());
        sqlMedia.setGenres(media.getGenre());

        return sqlMedia;
    }

    private void notFound() {
        throw new EntityNotFoundException("Media not found");
    }

    private Media setUpdate(Media old, Media mediaUpdate) {
        mediaUpdate.setId(old.getId());
        if (mediaUpdate.getTitle() == null || mediaUpdate.getTitle().isEmpty()) {
            mediaUpdate.setTitle(old.getTitle());
        }
        if (mediaUpdate.getDescription() == null || mediaUpdate.getDescription().isEmpty()) {
            mediaUpdate.setDescription(old.getDescription());
        }
        if (mediaUpdate.getMediaType() == null) {
            mediaUpdate.setMediaType(old.getMediaType());
        }
        if (mediaUpdate.getReleaseYear() == null) {
            mediaUpdate.setReleaseYear(old.getReleaseYear());
        }
        if (mediaUpdate.getAgeRestriction() == null) {
            mediaUpdate.setAgeRestriction(old.getAgeRestriction());
        }
        if (mediaUpdate.getGenre() == null) {
            mediaUpdate.setGenre(old.getGenre());
        }
        return mediaUpdate;
    }
}
