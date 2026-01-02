package at.technikum.application.service;

import at.technikum.application.dto.auth.UserCreateDto;
import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.UnprocessableEntityException;
import at.technikum.application.model.User;
import at.technikum.application.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public User register(UserCreateDto userCreate) {
        if (userCreate.isUser()) {
            User user = createToUser(userCreate);
            user.setId(UUID.randomUUID());
            Optional<User> registeredUser = this.userRepository.save(user);
            if (registeredUser.isEmpty()) {
                throw new UnprocessableEntityException("User already exists");
            }
            return registeredUser.get();
        }
        throw new EntityNotFoundException("Not enough parameters");
    }

    public UserLoggedInDto createToken(UserLoginDto userLogin) {
        if (userLogin.bothHere()) {
            Optional<UserLoggedInDto> userLoggedIn = this.userRepository.login(userLogin);
            if(userLoggedIn.isEmpty()) {
                throw new EntityNotFoundException("Username and password dont match");
            }
            return userLoggedIn.get().newToken();
        }
        throw new EntityNotFoundException("Not enough parameters");
    }

    private User createToUser(UserCreateDto userCreate) {
        User user = new User();
        user.setUsername(userCreate.getUsername());
        user.setEmail(userCreate.getEmail());
        user.setUserType(userCreate.getUserType());
        user.setPassword(userCreate.getPassword1());
        return user;
    }
}
