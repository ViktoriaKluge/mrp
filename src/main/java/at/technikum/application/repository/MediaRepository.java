package at.technikum.application.repository;

import at.technikum.application.model.Favorites;
import at.technikum.application.model.Media;
import at.technikum.application.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaRepository {

    Optional<Media> findById(UUID id);
    List<Media> mediaList();
    Optional<Media> save(Media media);
    Optional<String> delete(Media media);
    Optional<Media> update(Media media);
    Optional<Favorites> addFavorite(Favorites favorite);
    Optional<String> deleteFavorite(Favorites favorite);
}
