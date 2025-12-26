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

    public List<Rating> ratings(User user) {
        return this.userRepository.ratings(user);
    }

    public List<Media> favorites(User user) {
        return  this.userRepository.favorites(user);
    }

    public UserLoggedInDto update(UserUpdateDto update) {
        if (update.isUpdate()){
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
