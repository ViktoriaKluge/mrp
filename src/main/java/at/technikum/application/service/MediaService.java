package at.technikum.application.service;

import at.technikum.application.exception.EntityNotFoundException;
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

    public List<Media> findAll() {
        return mediaRepository.mediaList();
    }

    public Media findById(UUID id) {
        Media media = mediaRepository.findById(id);
        if (media == null) {
            throw new EntityNotFoundException("Media not found");
        }
        return media;
    }

    public void create(Media media) {
        mediaRepository.save(media);
    }

    public void update(Media media) {
        mediaRepository.save(media);
    }

    public void delete(String id) {
        mediaRepository.delete(id);
    }
}
