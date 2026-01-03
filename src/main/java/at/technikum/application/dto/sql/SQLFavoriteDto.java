package at.technikum.application.dto.sql;

import at.technikum.application.enums.MediaType;

import java.util.UUID;

public class SQLFavoriteDto {
    private UUID userId;
    private UUID mediaId;
    private String title;
    private MediaType mediaType;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
}
