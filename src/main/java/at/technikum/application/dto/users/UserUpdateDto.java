package at.technikum.application.dto.users;

import at.technikum.application.exception.UnprocessableEntityException;

import java.util.UUID;

public class UserUpdateDto {

    private UUID id;
    private String username;
    private String passwordOld;
    private String passwordNew1;
    private String passwordNew2;

    public UserUpdateDto() {

    }

    public boolean isUpdate() {
        return testUsername() && testUpdatePassword();
    }

    private boolean testUsername() {
        if (username == null || username.isEmpty()) {
            throw new UnprocessableEntityException("Username cannot be empty");
        }
        return true;
    }

    private boolean testUpdatePassword() {
        if(passwordNew1 == null || passwordNew1.isEmpty()) {
            return true;
        } else if(!passwordNew1.equals(passwordNew2)) {
            throw new UnprocessableEntityException("New Passwords do not match");
        }
        return true;
        // returned immer true oder schmeißt Exception
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

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPasswordOld() {
        return passwordOld;
    }

    public void setPasswordOld(String passwordOld) {
        this.passwordOld = passwordOld;
    }

    public String getPasswordNew1() {
        return passwordNew1;
    }

    public void setPasswordNew1(String passwordNew1) {
        this.passwordNew1 = passwordNew1;
    }

    public String getPasswordNew2() {
        return passwordNew2;
    }

    public void setPasswordNew2(String passwordNew2) {
        this.passwordNew2 = passwordNew2;
    }
}
