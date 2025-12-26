package at.technikum.application.dto.auth;

import at.technikum.application.enums.UserType;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

public class UserCreateDto {
    private String username;
    private String password1;
    private String password2;
    private String email;
    private String userType;

    public UserCreateDto() {

    }

    public boolean isUser() {
        return testUsername() && testUserType() && testPassword() && testEmail();
    }

    private boolean testUsername() {
        return this.username != null && !this.username.isEmpty();
    }

    private boolean testUserType() {
        if (userType == null ) {
            return false;
        } else return userType.equals(UserType.Admin.getType()) || userType.equals(UserType.User.getType());
    }

    private boolean testPassword() {
        if (password1 == null || password1.isEmpty()) {
            return false;
        } else return password1.equals(password2);
    }

    private boolean testEmail() {
        try {
            InternetAddress address = new InternetAddress(email);
            address.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        if (userType.equals("Admin")) {
            return UserType.Admin;
        } else if (userType.equals("User")) {
            return UserType.User;
        }
        return null;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
