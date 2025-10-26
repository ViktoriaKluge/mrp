package at.technikum.application.repository;

import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.dto.users.UserUpdateDto;
import at.technikum.application.dto.users.UserUpdatedDto;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.util.List;

public interface UserRepository {

    User findByUsername(String username);
    User findByEmail(String email);
    User findByID(String id);
    List<User> userList();
    List<Rating> ratings(String id);
    List<Media> favorites(String id);
    UserUpdatedDto update(String id, UserUpdateDto update);
    String delete(String id);
    User save(User user);
    UserLoginDto login(UserLoginDto userLoginDto);
}
