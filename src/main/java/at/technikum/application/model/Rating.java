package at.technikum.application.model;

import java.util.ArrayList;
import java.util.List;

public class Rating {
    private String id;
    private String comment;
    private Integer stars;

    public Rating() {

    }

    public Rating(String id, Integer rating) {
       this.comment = id;
       this.stars = rating;
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
        return String.format("{media id:%s, rating:%s}", this.comment, this.stars);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String mediaId) {
        this.comment = mediaId;
    }

    public Integer getRating() {
        return stars;
    }

    public void setRating(Integer rating) {
        this.stars = rating;
    }
}
