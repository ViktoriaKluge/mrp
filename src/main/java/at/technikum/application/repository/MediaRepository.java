package at.technikum.application.repository;

import at.technikum.application.model.Media;

import java.util.List;
import java.util.Optional;

public interface MediaRepository {

    Media findById(String id);
    List<Media> mediaList();
    Media save(Media media);
    String delete(String id);
    Media update(Media media);
    Media addRating(String id);
    Media removeRating(String id);
    Media addFavorite(String id);
    Media removeFavorite(String id);
}
