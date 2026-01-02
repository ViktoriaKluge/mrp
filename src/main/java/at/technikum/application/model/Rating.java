package at.technikum.application.model;

import at.technikum.application.enums.Stars;

import java.time.Instant;
import java.util.UUID;

public class Rating {
    private UUID id;
    private String comment;
    private Stars stars;
    private Instant createdAt;
    private boolean visibility;
    private User creator;
    private Media ratedMedia;

    public Rating() {
        this.createdAt = Instant.now();
        this.visibility = false;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Stars getStars() {
        return stars;
    }

    public void setStars(Stars stars) {
        this.stars = stars;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant timestamp) {
        this.createdAt = timestamp;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Media getRatedMedia() {
        return ratedMedia;
    }

    public void setRatedMedia(Media ratedMedia) {
        this.ratedMedia = ratedMedia;
    }
}
