package at.technikum.application.model;

import at.technikum.application.enums.UserType;

import java.util.List;

public class User {
    private String id;
    private String username;
    private String password;
    private String email;
    private UserType userType;
    private List<Rating> ratings;
    private List <Media> favorites;
    private List <String> favoriteGenre;

    public User() {

    }

    @Override
    public String toString() {
        return String.format("{id:%s, username:%s}", id, username);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public void setFavorites(List<Media> favorites) {
        this.favorites = favorites;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public List<Media> getFavorites() {
        return favorites;
    }

    public List<String> getFavoriteGenre() {
        return favoriteGenre;
    }

    public void setFavoriteGenre(List<String> favoriteGenre) {
        this.favoriteGenre = favoriteGenre;
    }
}
