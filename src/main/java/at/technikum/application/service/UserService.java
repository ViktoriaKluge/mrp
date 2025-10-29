package at.technikum.application.service;

import at.technikum.application.dto.media.MediaListAuthorizedDto;
import at.technikum.application.dto.rating.RatingListAuthorizedDto;
import at.technikum.application.dto.users.*;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.NotAuthorizedException;
import at.technikum.application.exception.UnprocessableEntityException;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;
import at.technikum.application.repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String id) {
        User user = this.userRepository.findByID(id);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
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

    public UserUpdatedDto update(UserUpdateDto update) {
        if (update.isUpdate()) {
            UserUpdatedDto userUpdatedDto = this.userRepository.update(update);
            if (userUpdatedDto == null) {
                throw new EntityNotFoundException("User not found or password invalid");
            }
            return userUpdatedDto;
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
