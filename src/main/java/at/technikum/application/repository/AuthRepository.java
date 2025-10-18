package at.technikum.application.repository;

import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.model.User;

import java.util.ArrayList;
import java.util.List;

public interface AuthRepository {
    List<User> userList = new ArrayList<>();


    User save(User user);
    UserLoginDto login(UserLoginDto userLoginDto);

    User findByUsername(String username);
    User findByEmail(String email);
    User findByID(String id);
}
