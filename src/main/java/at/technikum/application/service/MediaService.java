package at.technikum.application.service;

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

    public List<Media> findAll() {
        return this.mediaRepository.mediaList();
    }

    public Media findById(UUID id) {
        Optional<Media> media = this.mediaRepository.findById(id);
        if (media.isEmpty()) {
            notFound();
        }
        return media.get();
    }

    public Media create(Media media) {
        Optional<Media> createdMedia = this.mediaRepository.save(media);
        if (createdMedia.isEmpty()) {
            throw new UnprocessableEntityException("Media already exists");
        }
        return createdMedia.get();
    }

    public Media update(Media newMedia) {
        Optional <Media> updated = this.mediaRepository.update(newMedia);
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

    private void notFound(){
        throw new EntityNotFoundException("Media not found");
    }
}
