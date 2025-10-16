package at.technikum.application.repository;

public interface FavoritesRepository {
    boolean find(String id);

    void delete(String id);

    void add(String id);
}
