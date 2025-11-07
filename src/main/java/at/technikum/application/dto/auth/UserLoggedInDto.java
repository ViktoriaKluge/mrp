package at.technikum.application.dto.auth;

public class UserLoggedInDto {
    private String username;
    private String token;
    private String id;

    public UserLoggedInDto() {

    }

    public UserLoggedInDto(String username, String id) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
