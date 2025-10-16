package at.technikum.application.model;

import java.util.ArrayList;
import java.util.List;

public class Rating {
    String mediaId;
    Integer rating;

    public Rating() {

    }

    public Rating(String id, Integer rating) {
       this.mediaId = id;
       this.rating = rating;
    }

    public List<Rating> createMockRatings(){
        List<Rating> ratingMock = new ArrayList<>();
        ratingMock.add(new Rating("147",1));
        ratingMock.add(new Rating("369",4));
        ratingMock.add(new Rating("9761",3));
        ratingMock.add(new Rating("369",2));
        return ratingMock;
    }

    @Override
    public String toString() {
        return String.format("{media id:%s, rating:%s}", this.mediaId, this.rating);
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
