package at.technikum.application.repository;

import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.dto.users.UserUpdateDto;
import at.technikum.application.dto.users.UserUpdatedDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;
import java.util.List;

public class MemoryUserRepository implements UserRepository {

    List<User> users;

    public MemoryUserRepository() {
        User user = new User();
        this.users = user.createMockUsers();
    }

    @Override
    public List<User> userList() {
        return this.users;
    }

    @Override
    public List<Rating> ratings(String id) {
        Rating rating = new Rating();
        return rating.createMockRatings();
    }

    @Override
    public List<Media> favorites(String id) {
        Media media = new Media();
        return media.createMockMedia();
    }

    @Override
    public UserUpdatedDto update(UserUpdateDto update) {
        for (User user : users) {
            if (user.getId().equals(update.getId())) {
                if(user.getPassword().equals(update.getPasswordOld())) {
                    user.setUsername(update.getUsername());
                    user.setPassword(checkPassword(update));
                    return updateToUpdated(update,user);
                }
            }
        }
        return null;
    }

    @Override
    public String delete(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                String username = user.getUsername();
                users.remove(user);
                return username;
            }
        }
       return null;
    }

    @Override
    public User save(User user) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())
                    || u.getEmail().equals(user.getEmail())) {
                return null;
            }
        }
        this.users.add(user);
        return user;
    }

    @Override
    public UserLoggedInDto login(UserLoginDto userLogin) {
        User checkUser = findByUsername(userLogin.getUsername());
        if(checkUser == null) {
            return null;
        }
        if (checkUser.getPassword().equals(userLogin.getPassword())) {
            return new UserLoggedInDto(checkUser.getUsername(),checkUser.getId());
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public User findByID(String id) {
        for (User u : users) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    private String checkPassword(UserUpdateDto update) {
        if (update.getPasswordNew1()!=null && !update.getPasswordNew1().isEmpty()) {
            return update.getPasswordNew1();
        }
        return update.getPasswordOld();
    }

    private UserUpdatedDto updateToUpdated(UserUpdateDto update, User user) {
        UserUpdatedDto userUpdated = new UserUpdatedDto();
        userUpdated.setUsername(update.getUsername());
        userUpdated.setEmail(user.getEmail());
        userUpdated.setId(user.getId());
        return userUpdated;
    }
}
