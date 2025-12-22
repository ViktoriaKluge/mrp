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

    public User register(UserCreateDto userCreateDto) {
        if (userCreateDto.isUser()) {
            User user = createToUser(userCreateDto);
            user.setId(UUID.randomUUID());
            Optional<User> registeredUser = userRepository.save(user);
            if (registeredUser.isEmpty()) {
                throw new UnprocessableEntityException("User already exists");
            }
            return registeredUser.get();
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
            Optional<UserLoggedInDto> userLoggedInDto = this.userRepository.login(userLoginDto);
            if(userLoggedInDto.isEmpty()) {
                throw new EntityNotFoundException("Username and password dont match");
            }
            return userLoggedInDto.get().newToken();
        }
        throw new EntityNotFoundException("Not enough parameters");
    }
}
