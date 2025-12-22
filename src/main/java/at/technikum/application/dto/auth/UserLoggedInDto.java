package at.technikum.application.dto.auth;

import java.util.UUID;

public class UserLoggedInDto {
    private String username;
    private String token;
    private UUID id;

    public UserLoggedInDto() {

    }

    public UserLoggedInDto(String username, UUID id) {
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserLoggedInDto newToken() {
        this.setToken(username+"-mrpToken");
        return this;
    }
}
