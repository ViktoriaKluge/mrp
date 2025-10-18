package at.technikum.application.repository;

import at.technikum.application.dto.users.UserUpdateDto;
import at.technikum.application.dto.users.UserUpdatedDto;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.util.List;

public interface UserRepository {

    User find(String id);
    List<User> findAll();
    List<Rating> ratings(String id);
    List<Media> favorites(String id);
    UserUpdatedDto update(String id, UserUpdateDto update);
    String delete(String id);
}
