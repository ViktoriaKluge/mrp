package at.technikum.application.repository;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.model.Favorites;
import at.technikum.application.model.Media;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DbMediaRespository implements MediaRepository {
    private final ConnectionPool connectionPool;

    public DbMediaRespository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Optional<Media> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Media> mediaList() {
        return List.of();
    }

    @Override
    public Optional<Media> save(Media media) {
        return Optional.empty();
    }

    @Override
    public Optional<String> delete(Media media) {
        return Optional.empty();
    }

    @Override
    public Optional<Media> update(Media media) {
        return Optional.empty();
    }

    @Override
    public Optional<Favorites> addFavorite(Favorites favorite) {
        return Optional.empty();
    }

    @Override
    public Optional<String> deleteFavorite(Favorites favorite) {
        return Optional.empty();
    }
}
