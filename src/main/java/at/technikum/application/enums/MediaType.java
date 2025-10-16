package at.technikum.application.enums;

public enum MediaType {
    MOVIE("movie"),
    SERIES("series"),
    GAME("game");

    private final String type;

    MediaType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
