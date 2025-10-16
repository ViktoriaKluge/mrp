package at.technikum.application.repository;

import at.technikum.application.dto.UserLogin;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.UnprocessableEntityException;
import at.technikum.application.model.User;
import java.util.List;

public class MemoryAuthRepository implements AuthRepository {
    List<User> users;

    public MemoryAuthRepository() {
        User user = new User();
        this.users = user.createMockUsers();
    }

    @Override
    public User save(User user) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())
                    || u.getEmail().equals(user.getEmail())) {
                return null;
            }
        }
        this.users.add(user);
        return user;
    }

    @Override
    public UserLogin login(UserLogin userLogin) {
        User checkUser = findByUsername(userLogin.getUsername());
        if (checkUser.getPassword().equals(userLogin.getPassword())) {
            return userLogin;
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        throw new EntityNotFoundException("User not found");
    }

    @Override
    public User findByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        throw new RuntimeException(new EntityNotFoundException());
    }

    @Override
    public User findByID(String id) {
        for (User u : users) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        throw new RuntimeException(new EntityNotFoundException());
    }

}
