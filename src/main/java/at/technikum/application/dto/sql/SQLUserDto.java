package at.technikum.application.dto.sql;

import at.technikum.application.enums.UserType;

import java.util.UUID;

public class SQLUserDto {
    private UUID id;
    private String username;
    private String email;
    private UserType userType;

    public SQLUserDto() {

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
}
