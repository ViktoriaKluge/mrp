package at.technikum.application.repository;

import at.technikum.application.model.Like;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepository {


    Optional<String> delete(Rating rating);
    Optional<Rating> save(Rating rating);
    Optional<Rating> update(Rating rating);
    Optional<Like> like(Rating rating, User user);
    Optional<Rating> confirm(Rating rating);
    List<Rating> findAll();
    Optional<Rating> findByID(UUID id);
}
