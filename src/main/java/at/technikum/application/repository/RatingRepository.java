package at.technikum.application.repository;

import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepository {


    Optional<String> delete(Rating rating);
    Optional<Rating> save(Rating rating);
    Optional<Rating> update(Rating rating);
    Optional<Rating> like(Rating rating);
    Optional<Rating> dislike(Rating rating);
    Optional<Rating> unlike(Rating rating);
    Optional<Rating> confirm(Rating rating);
    List<Rating> findAll();
}
