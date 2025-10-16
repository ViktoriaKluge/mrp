package at.technikum.application.service;

import at.technikum.application.dto.UserUpdate;
import at.technikum.application.dto.UserUpdated;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.UnprocessableEntityException;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;
import at.technikum.application.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String id) {
        User user = this.userRepository.find(id);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }

    public List<User> getAll() {
        List<User> users = this.userRepository.findAll();
        if (users.isEmpty()) {
            throw new EntityNotFoundException("Users not found");
        }
        return users;
    }

    public List<Rating> ratings(String id) {
        List<Rating> ratings = this.userRepository.ratings(id);
        if (ratings.isEmpty()) {
            throw new EntityNotFoundException("Ratings not found");
        }
        return ratings;
    }

    public List<Media> favorites(String id) {
        List<Media> favorites = this.userRepository.favorites(id);
        if (favorites.isEmpty()) {
            throw new EntityNotFoundException("Favorites not found");
        }
        return favorites;
    }

    public UserUpdated update(String id, UserUpdate update) {
        if (update.isUpdate()) {
            UserUpdated userUpdated = this.userRepository.update(id, update);
            if (userUpdated == null) {
                throw new EntityNotFoundException("User not found or password invalid");
            }
            return userUpdated;
        }
        throw new UnprocessableEntityException("Not enough parameters");
    }

    public String delete(String id) {
        String deleted = this.userRepository.delete(id);
        if (deleted == null) {
            throw new EntityNotFoundException("User not found");
        }
        return deleted;
    }
}
