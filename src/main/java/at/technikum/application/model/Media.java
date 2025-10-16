package at.technikum.application.model;

import at.technikum.application.enums.MediaType;

import java.util.List;

public class Media {
    String id;
    String title;
    String description;
    List<String> genre;
    MediaType mediaType;
    Integer releaseYear;
    Integer ageRestriction;

    public Media() {

    }

    public Media(String id, String title, String description, List<String> genre, Integer releaseYear, Integer ageRestriction) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.ageRestriction = ageRestriction;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("media id:%s, title:%s\n", id, title);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(Integer ageRestriction) {
        this.ageRestriction = ageRestriction;
    }
}
