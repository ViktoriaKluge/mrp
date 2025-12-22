package at.technikum.application.service;

import at.technikum.application.model.Media;
import at.technikum.application.model.User;
import at.technikum.application.repository.UserRepository;
import at.technikum.server.http.ContentType;

import java.util.List;

public class RecommendationService {
    private final UserRepository userRepository;

    public RecommendationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Media> getRecommendations(User user, ContentType contentType) {
        Media media = new Media();
        return media.createMockMedia();
    }
}
