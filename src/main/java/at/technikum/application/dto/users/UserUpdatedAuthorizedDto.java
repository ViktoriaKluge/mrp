package at.technikum.application.dto.users;

public class UserUpdatedAuthorizedDto {
    private String token;
    private UserUpdatedDto userUpdatedDto;

    public UserUpdatedAuthorizedDto() {

    }

    public UserUpdatedAuthorizedDto(String token, UserUpdatedDto userUpdatedDto) {
        this.token = token;
        this.userUpdatedDto = userUpdatedDto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserUpdatedDto getUserUpdated() {
        return userUpdatedDto;
    }

    public void setUserUpdated(UserUpdatedDto userUpdatedDto) {
        this.userUpdatedDto = userUpdatedDto;
    }
}
