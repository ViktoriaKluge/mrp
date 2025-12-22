package at.technikum.application.dto.users;

import at.technikum.application.model.User;

public class UserWithTokenDto {
    private User user;
    private String token;

    public UserWithTokenDto() {

    }

    public UserWithTokenDto(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
