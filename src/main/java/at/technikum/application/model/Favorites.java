package at.technikum.application.model;

import java.util.UUID;

public class Favorites {
    private UUID id;
    private User user;
    private Media media;

    public Favorites() {

    }

    public Favorites(UUID id, User user, Media media) {
        this.id = id;
        this.user = user;
        this.media = media;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
