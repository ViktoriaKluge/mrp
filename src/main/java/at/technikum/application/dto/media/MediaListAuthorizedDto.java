package at.technikum.application.dto.media;

import at.technikum.application.model.Media;

import java.util.List;

public class MediaListAuthorizedDto {
    private String token;
    private List<Media> mediaList;

    public MediaListAuthorizedDto() {

    }

    public MediaListAuthorizedDto(String token, List<Media> mediaList) {
        this.token = token;
        this.mediaList = mediaList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Media> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<Media> mediaList) {
        this.mediaList = mediaList;
    }
}
