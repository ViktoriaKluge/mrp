package at.technikum.application.repository;

import at.technikum.application.dto.UserLogin;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoryUserRepository implements UserRepository {

    @Override
    public Optional<User> find(String id) {

        if (id.equals("1234")) {
            User user = new User();
            user.setId(id);
            user.setUsername("username");

            return Optional.of(user);
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public List<Rating> ratings(String id) {
        List<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating("147",1));
        ratings.add(new Rating("369",2));
        ratings.add(new Rating("9761",3));
        ratings.add(new Rating("369",2));
        return ratings;
    }

    @Override
    public List<Media> favorites(String id) {
        return List.of();
    }

    @Override
    public User update(User update) {
        update.setUsername(update.getUsername());
        return update;
    }

    @Override
    public void delete(String id) {

    }


}
