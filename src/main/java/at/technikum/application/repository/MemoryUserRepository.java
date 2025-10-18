package at.technikum.application.repository;

import at.technikum.application.dto.users.UserUpdateDto;
import at.technikum.application.dto.users.UserUpdatedDto;
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
    public User find(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
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
    public UserUpdatedDto update(String id, UserUpdateDto update) {
        for (User user : users) {
            if (user.getId().equals(id)) {
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

    private String checkPassword(UserUpdateDto update) {
        if (update.getPasswordNew1()!=null && !update.getPasswordNew1().isEmpty()) {
            return update.getPasswordNew1();
        }
        return update.getPasswordOld();
    }

    private UserUpdatedDto updateToUpdated(UserUpdateDto update, User user) {
        UserUpdatedDto userUpdatedDto = new UserUpdatedDto();
        userUpdatedDto.setUsername(update.getUsername());
        userUpdatedDto.setEmail(user.getEmail());
        userUpdatedDto.setId(user.getId());
        return userUpdatedDto;
    }
}
