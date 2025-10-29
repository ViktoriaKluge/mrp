package at.technikum.application.service;

import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.UnprocessableEntityException;
import at.technikum.application.model.Media;
import at.technikum.application.repository.MediaRepository;

public class FavoritesService {

    private final MediaRepository mediaRepository;

    public FavoritesService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public Media add(String id) {
        Media media = this.mediaRepository.addFavorite(id);
        if (media==null){
            throw new UnprocessableEntityException("Media already exists");
        }
        return media;
    }

    public String delete(String id) {
        String title = this.mediaRepository.delete(id);
        if (title==null){
            throw new EntityNotFoundException("Media Not Found");
        }
        return title;
    }

}
