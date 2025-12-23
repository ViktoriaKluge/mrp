package at.technikum.application.service;

import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.UnprocessableEntityException;
import at.technikum.application.model.Favorites;
import at.technikum.application.model.Media;
import at.technikum.application.model.User;
import at.technikum.application.repository.MediaRepository;

import java.util.UUID;

public class FavoritesService {

    private final MediaRepository mediaRepository;

    public FavoritesService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public Media add(Favorites favorites) {
        Media media = this.mediaRepository.addFavorite(id);
        if (media==null){
            throw new UnprocessableEntityException("Media already exists");
        }
        return media;
    }

    public String delete(Favorites favorites) {
        String title = this.mediaRepository.delete(id);
        if (title==null){
            throw new EntityNotFoundException("Media Not Found");
        }
        return title;
    }

}
