package at.technikum.application.model;

import java.util.List;

public class Rating {
    String mediaId;
    List<Integer> ratings;

    public Rating() {

    }

    public Rating(String id, Integer rating) {
        this.mediaId = id;
        this.ratings.add(rating);
    }

    @Override
    public String toString() {
        String ratingsStr = (ratings == null)
                ? "[]"
                : ratings.stream()
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.joining(", ", "[", "]"));
        return String.format("{media id:%s, ratings:%s}", mediaId, ratingsStr);
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings;
    }
}
