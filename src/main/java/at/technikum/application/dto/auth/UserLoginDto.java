package at.technikum.application.dto.auth;

import at.technikum.application.exception.UnprocessableEntityException;

public class UserLoginDto {
    private String username;
    private String password;

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

    public boolean bothHere() {
        if (username == null || password == null || password.isEmpty() || username.isEmpty()) {
            throw new UnprocessableEntityException("Username and password are mandatory");
        }
        return true;
    }
}
