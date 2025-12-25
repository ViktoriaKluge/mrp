package at.technikum.application.model;

public class Like {
    private Rating rating;
    private User user;

    public Like(){

    }

    public Like(Rating rating, User user){
        this.rating = rating;
        this.user = user;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
