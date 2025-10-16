package at.technikum.application.dto;

import at.technikum.application.enums.UserType;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

public class UserUpdate {

    private String id;
    private String userName;
    private String passwordOld;
    private String passwordNew1;
    private String passwordNew2;

    public UserUpdate() {

    }
    // check, ob username möglich + ob id oder username gefunden werden
    // id final?
    // nur update, falls altes passwort stimmt
    // neues Passwort nur, wenn auch 2 neue passen

    public boolean isUser() {

        return testUsername() && testUpdatePassword();
    }

    private boolean testUsername() {
        return this.userName != null && !this.userName.isEmpty();
    }

    private boolean testUpdatePassword() {
        if (passwordNew1 == null || passwordNew1.isEmpty()) {
            return false;
        } else return passwordNew1.equals(passwordNew2);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
