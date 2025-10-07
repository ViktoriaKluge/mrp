package at.technikum.application.repository;

import at.technikum.application.model.Media;

import java.util.Optional;

public interface FavoritesRepository {

    boolean find(String id);
    void add(String id);
    void delete(String id);
}
