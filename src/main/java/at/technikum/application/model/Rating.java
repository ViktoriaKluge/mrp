package at.technikum.application.model;

import java.time.Instant;
import java.util.UUID;

public class Rating {
    private UUID id;
    private String comment;
    private Integer stars;
    private Instant timestamp;
    private boolean visibility;
    private User creator;
    private Media ratedMedia;

    public Rating() {
        this.timestamp = Instant.now();
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

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
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
