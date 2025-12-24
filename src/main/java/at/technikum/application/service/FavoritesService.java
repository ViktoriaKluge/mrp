package at.technikum.application.service;

import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.Favorites;
import at.technikum.application.model.Media;
import at.technikum.application.model.User;
import at.technikum.application.repository.MediaRepository;
import at.technikum.application.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FavoritesService {

    private final MediaRepository mediaRepository;

    public FavoritesService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public Favorites add(Favorites favorite) {
        Optional<Favorites> created = mediaRepository.addFavorite(favorite);
        if(created.isEmpty()){
            throw new EntityNotFoundException("Favorite already exists");
        }
        return created.get();
    }

    public String delete(Favorites favorite) {
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
