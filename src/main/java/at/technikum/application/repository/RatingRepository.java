package at.technikum.application.repository;

import at.technikum.application.model.Rating;
import java.util.List;

public interface RatingRepository {


    void delete(String id);

    void save(Rating rating);

    void update(Rating rating);

    Rating findById(String id);

    List<Rating> findAll();
}
