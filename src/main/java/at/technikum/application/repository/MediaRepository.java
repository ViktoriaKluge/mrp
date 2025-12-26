package at.technikum.application.repository;

import at.technikum.application.dto.sql.SQLFavoriteDto;
import at.technikum.application.model.Favorite;
import at.technikum.application.model.Media;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaRepository {

    Optional<Media> findById(UUID id);
    List<Media> mediaList();
    Optional<Media> save(Media media);
    Optional<String> delete(Media media);
    Optional<Media> update(Media media);
    Optional<SQLFavoriteDto> addFavorite(Favorite favorite);
    Optional<String> deleteFavorite(Favorite favorite);
}
