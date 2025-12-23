package at.technikum.application.model;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Rating {
    private UUID id;
    private String comment;
    private Integer stars;
    private Timestamp timestamp;
    private boolean visibility;
    private User creator;
    private Media ratedMedia;

    public Rating() {

    }

    public Rating(UUID id, String comment, Integer stars,  Timestamp timestamp,
                  User creator, Media ratedMedia) {
       this.id = id;
       this.comment = comment;
       this.stars = stars;
       this.timestamp = timestamp;
       this.visibility = false;
       this.creator = creator;
       this.ratedMedia = ratedMedia;

    }

    /*
    public List<Rating> createMockRatings(){
        List<Rating> ratingMock = new ArrayList<>();
        ratingMock.add(new Rating("147",1));
        ratingMock.add(new Rating("369",4));
        ratingMock.add(new Rating("9761",3));
        ratingMock.add(new Rating("369",2));
        return ratingMock;
    }

     */

    @Override
    public String toString() {
        return String.format("{media id:%s, rating:%s}", this.comment, this.stars);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Media getRatedMedia() {
        return ratedMedia;
    }

    public void setRatedMedia(Media ratedMedia) {
        this.ratedMedia = ratedMedia;
    }
}
