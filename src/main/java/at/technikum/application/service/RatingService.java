package at.technikum.application.service;

import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.Like;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;
import at.technikum.application.repository.RatingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public List<Rating> allRatings(){
        return ratingRepository.findAll();
    }

    public Rating findById(UUID id){
        Optional<Rating> rating = this.ratingRepository.findByID(id);
        if (rating.isEmpty()) {
            notFound();
        }
        return rating.get();
    }

    public Rating create(Rating rating) {
        Optional<Rating> created = ratingRepository.save(rating);
        if(created.isEmpty()){
            throw new EntityNotFoundException("Rating already exists");
        }
        return created.get();
    }

    public Rating update(Rating rating) {
        Optional<Rating> updated = ratingRepository.update(rating);
        if(updated.isEmpty()){
            notFound();
        }
        return updated.get();
    }

    public Like like(Rating rating, User user) {
        Optional<Like> liked = ratingRepository.like(rating, user);
        if(liked.isEmpty()){
            notFound();
        }
        return liked.get();
    }

    public Rating confirm(Rating rating) {
        Optional<Rating> confirmed = ratingRepository.confirm(rating);
        if(confirmed.isEmpty()){
            notFound();
        }
        return confirmed.get();
    }

    public String delete(Rating rating) {
        Optional<String> deleted = ratingRepository.delete(rating);
        if(deleted.isEmpty()){
            notFound();
        }
        return deleted.get();
    }

    private void notFound(){
        throw new EntityNotFoundException("Rating not found");
    }
}
