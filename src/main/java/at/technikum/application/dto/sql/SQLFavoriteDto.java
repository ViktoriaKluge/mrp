package at.technikum.application.dto.sql;

import java.util.UUID;

public class SQLFavoriteDto {
    private UUID userId;
    private UUID mediaId;

    public SQLFavoriteDto() {

    }

    public SQLFavoriteDto(UUID userId, UUID mediaId) {
        this.userId = userId;
        this.mediaId = mediaId;
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
}
