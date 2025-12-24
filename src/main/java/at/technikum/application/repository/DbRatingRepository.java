package at.technikum.application.repository;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DbRatingRepository implements RatingRepository{
    private final ConnectionPool connectionPool;

    public DbRatingRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Optional<String> delete(Rating rating) {
        return Optional.empty();
    }

    @Override
    public Optional<Rating> save(Rating rating) {
        return Optional.empty();
    }

    @Override
    public Optional<Rating> update(Rating rating) {
        return Optional.empty();
    }

    @Override
    public Optional<Rating> like(Rating rating) {
        return Optional.empty();
    }

    @Override
    public Optional<Rating> dislike(Rating rating) {
        return Optional.empty();
    }

    @Override
    public Optional<Rating> unlike(Rating rating) {
        return Optional.empty();
    }

    @Override
    public Optional<Rating> confirm(Rating rating) {
        return Optional.empty();
    }

    @Override
    public List<Rating> findAll() {
        return List.of();
    }
}
