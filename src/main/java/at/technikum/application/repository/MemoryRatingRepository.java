package at.technikum.application.repository;


import at.technikum.application.model.Rating;
import java.util.List;

public class MemoryRatingRepository implements RatingRepository {

    @Override
    public void delete(String id) {

    }

    @Override
    public void save(Rating rating) {

    }

    @Override
    public void update(Rating rating) {

    }

    @Override
    public Rating findById(String id) {
        return null;
    }

    @Override
    public List<Rating> findAll() {
        return List.of();
    }
}
