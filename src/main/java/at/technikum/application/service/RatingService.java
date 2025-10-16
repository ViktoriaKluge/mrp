package at.technikum.application.service;

import at.technikum.application.model.Rating;
import at.technikum.application.repository.RatingRepository;

import java.util.List;

public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public List<Rating> allRatings(){
        return ratingRepository.findAll();
    }

    public Rating findById(String id) {
        return ratingRepository.findById(id);
               // .orElseThrow(EntityNotFoundException::new);
    }

    public void create(Rating rating) {
        ratingRepository.save(rating);
    }

    public void update(Rating rating) {
        ratingRepository.update(rating);
    }

    public void delete(String id) {
        ratingRepository.delete(id);
    }
}
