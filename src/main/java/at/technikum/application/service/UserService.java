package at.technikum.application.service;

import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.users.*;
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

    /*
    public User getUser(String id) {
        User user = this.userRepository.findByID(id);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }

     */

    public List<Rating> ratings(UUID id) {
        List<Rating> ratings = this.userRepository.ratings(id);
        /*
        wenn die Liste leer ist, muss das kein Fehler sein
        if (ratings.isEmpty()) {
            throw new EntityNotFoundException("Ratings not found");
        }

         */
        return ratings;
    }

    public List<Media> favorites(UUID id) {
        List<Media> favorites = this.userRepository.favorites(id);
        /*
        wenn die Liste leer ist, muss das kein Fehler sein
         if (favorites.isEmpty()) {
            throw new EntityNotFoundException("Favorites not found");
        }
         */

        return favorites;
    }

    public UserLoggedInDto update(User user, UserUpdateDto update) {
        if (update.isUpdate() && user.getPassword().equals(update.getPasswordOld())) {
            Optional<UserLoggedInDto> userUpdatedOpt = this.userRepository.update(update);
            UserLoggedInDto updatedUser = userUpdatedOpt.get();
            updatedUser.newToken();
            return updatedUser;
        }
        throw new UnprocessableEntityException("Not enough parameters");
    }

    public String delete(User user) {
        Optional<String> deleted = this.userRepository.delete(user);
        if (deleted.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return deleted.get();
    }

}
