package at.technikum.application.repository;

import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.dto.users.UserUpdateDto;
import at.technikum.application.enums.MediaType;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByID(UUID id);
    List<User> userList();
    List<Rating> ratings(User user);
    List<Media> favorites(User user);
    Optional<UserLoggedInDto> update(UserUpdateDto update);
    Optional<String> delete(User user);
    Optional<User> save(User user);
    Optional<UserLoggedInDto> login(UserLoginDto userLoginDto);
    List<Media> recommendations(User user, MediaType mediaType);
    List<User> leaderboard();
}
