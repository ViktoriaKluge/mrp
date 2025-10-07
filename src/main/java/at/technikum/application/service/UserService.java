package at.technikum.application.service;

import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;
import at.technikum.application.repository.UserRepository;

import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // maybe private find, weil ratings und favs auch brauchen und delete
    public User getUser(String id) {
        return userRepository.find(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    // Leaderboard?
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    public List<Rating> ratings(String id) {
        return this.userRepository.ratings(id);
    }

    public List<Media> favorites(String id) {
        return this.userRepository.favorites(id);
    }

    public void update(String id, User update) {
        User user = this.userRepository.find(id)
               .orElseThrow(EntityNotFoundException::new);

        user.setUsername(update.getUsername());

        this.userRepository.update(user);
    }

    public void delete(String id) {
        userRepository.delete(id);
    }
}
