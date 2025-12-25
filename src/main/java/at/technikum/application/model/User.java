package at.technikum.application.model;

import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.enums.UserType;
import at.technikum.application.exception.NotAuthorizedException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private UserType userType;

    public User() {

    }

    public User(UUID id, String username, String password, String email, UserType userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
    }

    /*
    public List<User> createMockUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("1234","testuser","safepassword","test@example.com",UserType.Admin));
        users.add(new User("9632","userlein","securepassword","example@test.com",UserType.User));
        return users;
    }

     */

    @Override
    public String toString() {
        return String.format("{id:%s, username:%s}", id, username);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isAdmin() {
        if (userType == UserType.Admin){
            return true;
        }
        throw new NotAuthorizedException("You need to be an admin to do this");
    }
}
