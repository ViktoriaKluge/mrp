package at.technikum.application.dto.rating;

import at.technikum.application.model.Rating;

import java.util.List;

public class RatingListAuthorizedDto {
    private String token;
    private List<Rating>  ratingList;

    public RatingListAuthorizedDto() {

    }

    public RatingListAuthorizedDto(String token, List<Rating> ratingList) {
        this.token = token;
        this.ratingList = ratingList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Rating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }
}
