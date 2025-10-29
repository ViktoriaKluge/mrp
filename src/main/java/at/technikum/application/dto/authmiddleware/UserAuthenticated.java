package at.technikum.application.dto.authmiddleware;

import at.technikum.application.model.User;

public class UserAuthenticated {
    private final User user;
    private final boolean authenticated;

    public UserAuthenticated(User user, boolean authenticated) {
        this.user = user;
        this.authenticated = authenticated;
    }


}
