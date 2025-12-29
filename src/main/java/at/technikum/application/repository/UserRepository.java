package at.technikum.application.repository;

import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.dto.sql.SQLFavoriteDto;
import at.technikum.application.dto.sql.SQLMediaDto;
import at.technikum.application.dto.sql.SQLRatingDto;
import at.technikum.application.dto.users.UserUpdateDto;
import at.technikum.application.enums.MediaType;
import at.technikum.application.model.LeaderboardEntry;
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
    Optional<UserLoggedInDto> update(UserUpdateDto update);
    Optional<String> delete(User user);
    Optional<User> save(User user);
    Optional<UserLoggedInDto> login(UserLoginDto userLoginDto);
    List<SQLMediaDto> recommendations(User user, MediaType mediaType);
    List<LeaderboardEntry> leaderboard();
}
