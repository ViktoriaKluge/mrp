package at.technikum.application.dto.sql;

import java.util.UUID;

public class SQLLikeDto {
    private UUID userId;
    private UUID ratingId;

    public SQLLikeDto() {

    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getRatingId() {
        return ratingId;
    }

    public void setRatingId(UUID ratingId) {
        this.ratingId = ratingId;
    }
}
