package at.technikum.application.service;

import at.technikum.application.model.User;
import at.technikum.application.repository.AuthRepository;

import java.util.UUID;

public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void register(User user) {
        user.setId(UUID.randomUUID().toString());

        this.authRepository.register(user);
    }

    public void login(String username, String password) {
        this.authRepository.login(username, password);
    }
}
