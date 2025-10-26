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

    public UserAuthorizedDto getUser(UserAuthorizeDto userAuthorizeDto) {
        String token = isAuthorized(userAuthorizeDto);
        User user = this.userRepository.findByID(userAuthorizeDto.getId());
        return new UserAuthorizedDto(user,token);
    }

    public UserListAuthorizedDto getAll(UserAuthorizeDto userAuthorizeDto) {
        String token = isAuthorized(userAuthorizeDto);
        List<User> users = this.userRepository.userList();
        if (users.isEmpty()) {
            throw new EntityNotFoundException("Users not found");
        }
        return new UserListAuthorizedDto(token,users);
    }

    public RatingListAuthorizedDto ratings(UserAuthorizeDto userAuthorizeDto) {
        String token = isAuthorized(userAuthorizeDto);
        List<Rating> ratings = this.userRepository.ratings(userAuthorizeDto.getId());
        if (ratings.isEmpty()) {
            throw new EntityNotFoundException("Ratings not found");
        }
        return new RatingListAuthorizedDto(token,ratings);
    }

    public MediaListAuthorizedDto favorites(UserAuthorizeDto userAuthorizeDto) {
        String token = isAuthorized(userAuthorizeDto);
        List<Media> favorites = this.userRepository.favorites(userAuthorizeDto.getId());
        if (favorites.isEmpty()) {
            throw new EntityNotFoundException("Favorites not found");
        }
        return new MediaListAuthorizedDto(token,favorites);
    }

    public UserUpdatedAuthorizedDto update(UserAuthorizeDto userAuthorizeDto, UserUpdateDto update) {
        String token = isAuthorized(userAuthorizeDto);
        if (update.isUpdate()) {
            UserUpdatedDto userUpdatedDto = this.userRepository.update(userAuthorizeDto.getId(), update);
            if (userUpdatedDto == null) {
                throw new EntityNotFoundException("User not found or password invalid");
            }
            return new UserUpdatedAuthorizedDto(token, userUpdatedDto);
        }
        throw new UnprocessableEntityException("Not enough parameters");
    }

    public String delete(UserAuthorizeDto userAuthorizeDto) {
        String token = isAuthorized(userAuthorizeDto);
        String deleted = this.userRepository.delete(userAuthorizeDto.getId());
        if (deleted == null) {
            throw new EntityNotFoundException("User not found");
        }
        return deleted;
    }

    private String isAuthorized(UserAuthorizeDto userAuthorizeDto) {
        User user = this.userRepository.findByID(userAuthorizeDto.getId());
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        String token = userAuthorizeDto.getToken();
        final String suffix = "-mrpToken";
        if (token == null || token.isEmpty() || !token.equals(user.getUsername()+suffix)) {
            throw new NotAuthorizedException("Not authorized");
        }
        return token;
    }
}
