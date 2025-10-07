package at.technikum.application.repository;

import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> find(String id);

    // Leaderboard
    List<User> findAll();

    List<Rating> ratings(String id);
    List<Media> favorites(String id);
    void update(User update);
    void delete(String id);
}
