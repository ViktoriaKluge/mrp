package at.technikum.application.repository;

import at.technikum.application.dto.UserLogin;
import at.technikum.application.model.User;

public class MemoryAuthRepository implements AuthRepository {
    @Override
    public User save(User user) {
        // check ob email / username schon vergeben?
        userList.add(user);
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
        for (User u : userList) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        for (User u : userList) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public User findByID(String id) {
        for (User u : userList) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

}
