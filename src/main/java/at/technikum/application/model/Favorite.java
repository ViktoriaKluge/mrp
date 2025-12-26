package at.technikum.application.model;

public class Favorite {
    private User user;
    private Media media;

    public Favorite() {

    }

    public Favorite(User user, Media media) {
        this.user = user;
        this.media = media;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
