package at.technikum.application.model;

import java.util.List;

public class User {
    private String id;
    private String username;
    private List<Rating> ratings;
    private List <Media> favorites;

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

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Rating rating) {
        this.ratings.add(rating);
    }

    public List <Media> getFavorites() {
        return favorites;
    }

    public void setFavorites (Media favorite) {
        this.favorites.add(favorite);
    }
}
