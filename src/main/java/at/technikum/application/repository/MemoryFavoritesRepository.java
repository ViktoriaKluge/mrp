package at.technikum.application.repository;

public class MemoryFavoritesRepository implements FavoritesRepository {
    @Override
    public boolean find(String id) {
        return false;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void add(String id) {

    }
}
