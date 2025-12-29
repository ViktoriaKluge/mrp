package at.technikum.application.repository;

import at.technikum.application.dto.sql.SQLFavoriteDto;
import at.technikum.application.dto.sql.SQLMediaDto;
import at.technikum.application.model.Favorite;
import at.technikum.application.model.Media;
import at.technikum.application.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaRepository {

    Optional<SQLMediaDto> findById(UUID id);
    Optional<Media> findByIdMedia(UUID id);
    List<SQLMediaDto> mediaList();
    Optional<SQLMediaDto> save(Media media);
    Optional<String> delete(Media media);
    Optional<SQLMediaDto> update(Media media);
    Optional<SQLFavoriteDto> addFavorite(Favorite favorite);
    Optional<String> deleteFavorite(Favorite favorite);
    List<SQLFavoriteDto> findFavsByUserId(User user);
}
