package at.technikum.application.repository;

import at.technikum.application.dto.UserLogin;
import at.technikum.application.dto.UserUpdate;
import at.technikum.application.dto.UserUpdated;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User find(String id);
    List<User> findAll();
    List<Rating> ratings(String id);
    List<Media> favorites(String id);
    UserUpdated update(String id, UserUpdate update);
    String delete(String id);
}
