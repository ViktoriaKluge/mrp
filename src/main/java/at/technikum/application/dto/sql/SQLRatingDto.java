package at.technikum.application.dto.sql;

import at.technikum.application.enums.Stars;

import java.time.Instant;
import java.util.UUID;

public class SQLRatingDto {
    private UUID id;
    private UUID userId;
    private UUID mediaId;
    private Stars stars;
    private String comment;
    private Instant createdAt;
    private boolean visibility;

    public SQLRatingDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getMediaId() {
        return mediaId;
    }

    public void setMediaId(UUID mediaId) {
        this.mediaId = mediaId;
    }

    public Stars getStars() {
        return stars;
    }


    public void setStars(Stars stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
