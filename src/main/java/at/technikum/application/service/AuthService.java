package at.technikum.application.service;

import at.technikum.application.dto.UserCreate;
import at.technikum.application.dto.UserLoggedIn;
import at.technikum.application.dto.UserLogin;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.User;
import at.technikum.application.repository.AuthRepository;
import at.technikum.application.repository.UserRepository;

import java.util.UUID;

public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public User register(UserCreate userCreate) {
        User user = createToUser(userCreate);

        user.setId(UUID.randomUUID().toString());

       return this.authRepository.save(user);

    }

    private User createToUser(UserCreate userCreate) {
        User user = new User();
        user.setUsername(userCreate.getUserName());
        user.setEmail(userCreate.getEmail());
        user.setUserType(userCreate.getUserType());
        user.setPassword(userCreate.getPassword1());

        return user;
    }

    public UserLoggedIn createToken(UserLogin userLogin) {
      userLogin = this.authRepository.login(userLogin);
      if (userLogin == null) {
          throw new RuntimeException(new EntityNotFoundException());
        }
      return newToken(userLogin.getUsername());
    }

    private UserLoggedIn newToken(String username) {
        UserLoggedIn userLoggedIn = new UserLoggedIn();
        userLoggedIn.setUsername(username);
        userLoggedIn.setToken(username+"-mrpToken");
        return userLoggedIn;
    }
}
