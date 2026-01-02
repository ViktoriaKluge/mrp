package at.technikum.application.service;

import at.technikum.application.dto.sql.SQLFavoriteDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.UnprocessableEntityException;
import at.technikum.application.model.Favorite;
import at.technikum.application.repository.MediaRepository;

import java.util.Optional;

public class FavoritesService {

    private final MediaRepository mediaRepository;

    public FavoritesService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public SQLFavoriteDto add(Favorite favorite) {
        Optional<SQLFavoriteDto> created = this.mediaRepository.addFavorite(favorite);
        if(created.isEmpty()){
            throw new UnprocessableEntityException("Favorite already exists");
        }
        return created.get();
    }

    public String delete(Favorite favorite) {
        Optional<String> deleted = this.mediaRepository.deleteFavorite(favorite);
        if(deleted.isEmpty()){
            throw new EntityNotFoundException("Favorite not found");
        }
        return deleted.get();
    }
}
