package at.technikum.application.service;

import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.Media;
import at.technikum.application.repository.MediaRepository;

import java.util.List;
import java.util.Optional;

public class MediaService {

    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public List<Media> findAll() {
        return mediaRepository.findAll();
    }

    public Media findById(String id) {
        return mediaRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
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
