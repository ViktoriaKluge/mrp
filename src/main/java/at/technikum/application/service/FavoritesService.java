package at.technikum.application.service;

import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.Favorite;
import at.technikum.application.repository.MediaRepository;

import java.util.Optional;

public class FavoritesService {

    private final MediaRepository mediaRepository;

    public FavoritesService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public Favorite add(Favorite favorite) {
        Optional<Favorite> created = mediaRepository.addFavorite(favorite);
        if(created.isEmpty()){
            throw new EntityNotFoundException("Favorite already exists");
        }
        return created.get();
    }

    public String delete(Favorite favorite) {
        Optional<String> deleted = mediaRepository.deleteFavorite(favorite);
        if(deleted.isEmpty()){
            notFound();
        }
        return deleted.get();
    }

    private void notFound(){
        throw new EntityNotFoundException("Favorite not found");
    }

}
