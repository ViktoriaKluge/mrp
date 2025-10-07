package at.technikum.application.repository;

import at.technikum.application.model.User;

public interface AuthRepository {

    void register(User user);
    void login(String username, String password);
}
