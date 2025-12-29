package at.technikum.application.service;

import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.sql.SQLFavoriteDto;
import at.technikum.application.dto.sql.SQLMediaDto;
import at.technikum.application.dto.sql.SQLRatingDto;
import at.technikum.application.dto.users.*;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.UnprocessableEntityException;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;
import at.technikum.application.repository.MediaRepository;
import at.technikum.application.repository.RatingRepository;
import at.technikum.application.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final MediaRepository mediaRepository;

    public UserService(UserRepository userRepository, RatingRepository ratingRepository, MediaRepository mediaRepository) {
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.mediaRepository = mediaRepository;
    }

    public List<SQLRatingDto> ratings(User user) {
        return this.ratingRepository.findAllByUserID(user);
    }

    public List<SQLFavoriteDto> favorites(User user) {
        return  this.mediaRepository.findFavsByUserId(user);
    }

    public UserLoggedInDto update(User user, UserUpdateDto update) {
        if (update.isUpdate()){
            update = setUpdate(user,update);
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

    private UserUpdateDto setUpdate(User user, UserUpdateDto update) {
        update.setId(user.getId());
        String psw = checkPassword(update);
        update.setPasswordNew1(psw);
        if (update.getEmail() == null || update.getEmail().isEmpty()) {
            update.setEmail(user.getEmail());
        }
        return update;
    }


    private String checkPassword(UserUpdateDto update) {
        if (update.getPasswordNew1()!=null && !update.getPasswordNew1().isEmpty()) {
            return update.getPasswordNew1();
        }
        return update.getPasswordOld();
    }
}
