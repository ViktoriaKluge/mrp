package at.technikum.application.repository;

import at.technikum.application.dto.sql.SQLLikeDto;
import at.technikum.application.dto.sql.SQLRatingDto;
import at.technikum.application.model.Like;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepository {


    Optional<SQLRatingDto> findByID(UUID id);
    Optional<Rating> findByIDRating(UUID id);
    Optional<SQLRatingDto> save(Rating rating);
    Optional<SQLRatingDto> update(Rating rating);
    Optional<SQLLikeDto> like(Like like);
    Optional<SQLRatingDto> confirm(Rating rating);
    Optional<String> delete(Rating rating);
    List<SQLRatingDto> findAll();
    List<SQLRatingDto> findAllByUserID(User user);

}
