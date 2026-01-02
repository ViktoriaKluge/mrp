package at.technikum.application.service;

import at.technikum.application.dto.sql.SQLLikeDto;
import at.technikum.application.dto.sql.SQLRatingDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.UnprocessableEntityException;
import at.technikum.application.model.Like;
import at.technikum.application.model.Rating;
import at.technikum.application.repository.RatingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public List<SQLRatingDto> allRatings(){
        return ratingRepository.findAll();
    }

    public Rating findById(UUID id){
        Optional<Rating> rating = this.ratingRepository.findByIDRating(id);
        if (rating.isEmpty()) {
            notFound();
        }
        return rating.get();
    }

    public SQLRatingDto create(Rating rating) {
        Optional<SQLRatingDto> created = this.ratingRepository.save(rating);
        if(created.isEmpty()){
            throw new UnprocessableEntityException("You can only rate media once");
        }
        return created.get();
    }

    public SQLRatingDto update(Rating old, Rating update) {
        Rating updateSet = setUpdate(old,update);
        Optional<SQLRatingDto> updated = this.ratingRepository.update(updateSet);
        if(updated.isEmpty()){
            notFound();
        }
        return updated.get();
    }

    public SQLLikeDto like(Like like) {
        Optional<SQLLikeDto> liked = this.ratingRepository.like(like);
        if(liked.isEmpty()){
            notFound();
        }
        return liked.get();
    }

    public SQLRatingDto confirm(Rating rating) {
        Optional<SQLRatingDto> confirmed = this.ratingRepository.confirm(rating);
        if(confirmed.isEmpty()){
            notFound();
        }
        return confirmed.get();
    }

    public String delete(Rating rating) {
        Optional<String> deleted = this.ratingRepository.delete(rating);
        if(deleted.isEmpty()){
            notFound();
        }
        return deleted.get();
    }

    private void notFound(){
        throw new EntityNotFoundException("Rating not found");
    }

    private Rating setUpdate(Rating old, Rating update) {
        update.setId(old.getId());
        if (update.getStars() == null) {
            update.setStars(old.getStars());
        }
        if (update.getComment() == null || update.getComment().isEmpty()) {
            update.setComment(old.getComment());
        }
        return update;
    }
}
