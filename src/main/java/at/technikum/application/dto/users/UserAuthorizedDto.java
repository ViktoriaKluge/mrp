package at.technikum.application.dto.users;

import at.technikum.application.model.User;

public class UserAuthorizedDto {
    private User user;
    private String token;

    public UserAuthorizedDto() {

    }

    public UserAuthorizedDto(User user, String token) {
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
