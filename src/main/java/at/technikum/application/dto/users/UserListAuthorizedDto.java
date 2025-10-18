package at.technikum.application.dto.users;

import at.technikum.application.model.User;

import java.util.List;

public class UserListAuthorizedDto {
    private String token;
    private List<User>  userList;

    public UserListAuthorizedDto() {

    }

    public UserListAuthorizedDto(String token, List<User> userList) {
        this.token = token;
        this.userList = userList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
