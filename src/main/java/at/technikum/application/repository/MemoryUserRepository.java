package at.technikum.application.repository;

import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.dto.users.UserUpdateDto;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MemoryUserRepository implements UserRepository {

    List<User> users;

    public MemoryUserRepository() {
        User user = new User();
    }

    @Override
    public List<User> userList() {
        return this.users;
    }

    @Override
    public List<Rating> ratings(UUID id) {
        Rating rating = new Rating();
        return rating.createMockRatings();
    }

    @Override
    public List<Media> favorites(UUID id) {
        Media media = new Media();
        return media.createMockMedia();
    }

    @Override
    public Optional<UserLoggedInDto> update(UserUpdateDto update) {
        for (User user : users) {
            if (user.getId().equals(update.getId())) {
                if(user.getPassword().equals(update.getPasswordOld())) {
                    user.setUsername(update.getUsername());
                    user.setPassword(checkPassword(update));
                    return Optional.of(updateToLoggedIn(update,user));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> delete(User user) {
        for (User u : users) {
            if (u.getId().equals(user.getId())) {
                String username = user.getUsername();
                users.remove(u);
                return Optional.of(username);
            }
        }
       return Optional.empty();
    }

    @Override
    public Optional<User> save(User user) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())
                    || u.getEmail().equals(user.getEmail())) {
                return Optional.empty();
            }
        }
        this.users.add(user);
        return Optional.of(user);
    }

    @Override
    public Optional<UserLoggedInDto> login(UserLoginDto userLogin) {
        Optional<User> checkUser = findByUsername(userLogin.getUsername());
        if(checkUser.isEmpty()) {
            return Optional.empty();
        }
        User foundUser = checkUser.get();
        if (foundUser.getPassword().equals(userLogin.getPassword())) {
            return Optional.of(new UserLoggedInDto(foundUser.getUsername(),foundUser.getId()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByID(UUID id) {
        for (User u : users) {
            if (u.getId().equals(id)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    private String checkPassword(UserUpdateDto update) {
        if (update.getPasswordNew1()!=null && !update.getPasswordNew1().isEmpty()) {
            return update.getPasswordNew1();
        }
        return update.getPasswordOld();
    }

    private UserLoggedInDto updateToLoggedIn(UserUpdateDto update, User user) {
        UserLoggedInDto userUpdated = new UserLoggedInDto();
        userUpdated.setUsername(update.getUsername());
        userUpdated.setId(user.getId());
        return userUpdated;
    }
}
