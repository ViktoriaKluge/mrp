package at.technikum.application.dto.recommendations;

import at.technikum.application.model.Media;

import java.util.List;

public class RecommendationsAuthorizedDto {
    private List<Media> recommendations;
    private String token;

    public RecommendationsAuthorizedDto(){

    }

    public RecommendationsAuthorizedDto(List<Media> recommendations, String token){

        this.recommendations = recommendations;
        this.token = token;
    }

    public List<Media> getRecommendations() {
        return recommendations;
    }

    public String getToken() {
        return token;
    }

    public void setRecommendations(List<Media> recommendations) {
        this.recommendations = recommendations;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
