package at.technikum.application.service;

import at.technikum.application.dto.auth.UserCreateDto;
import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.UnprocessableEntityException;
import at.technikum.application.model.User;
import at.technikum.application.repository.UserRepository;

import java.util.UUID;

public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public User register(UserCreateDto userCreateDto) {
        if (userCreateDto.isUser()) {
            User user = createToUser(userCreateDto);
            user.setId(UUID.randomUUID().toString());
            User registeredUser = userRepository.save(user);
            if (registeredUser == null) {
                throw new UnprocessableEntityException("User already exists");
            }
            return registeredUser;
        }
        throw new EntityNotFoundException("Not enough parameters");
    }

    private User createToUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setEmail(userCreateDto.getEmail());
        user.setUserType(userCreateDto.getUserType());
        user.setPassword(userCreateDto.getPassword1());
        return user;
    }

    public UserLoggedInDto createToken(UserLoginDto userLoginDto) {
        if (userLoginDto.bothHere()) {
            userLoginDto = this.userRepository.login(userLoginDto);
            if(userLoginDto == null) {
                throw new EntityNotFoundException("Username and password dont match");
            }
            return newToken(userLoginDto.getUsername());
        }
        throw new EntityNotFoundException("Not enough parameters");
    }

    private UserLoggedInDto newToken(String username) {
        UserLoggedInDto userLoggedInDto = new UserLoggedInDto();
        userLoggedInDto.setUsername(username);
        userLoggedInDto.setToken(username+"-mrpToken");
        return userLoggedInDto;
    }
}
