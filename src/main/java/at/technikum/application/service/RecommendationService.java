package at.technikum.application.service;

import at.technikum.application.dto.users.UserAuthorizeDto;
import at.technikum.application.model.Media;
import at.technikum.application.model.User;
import at.technikum.application.repository.MemoryUserRepository;

import java.util.List;

public class RecommendationService {
    private final MemoryUserRepository memoryUserRepository;

    public RecommendationService(MemoryUserRepository memoryUserRepository) {
        this.memoryUserRepository = memoryUserRepository;
    }

    public List<Media> getRecommendations(UserAuthorizeDto user, String genre) {
        Media media = new Media();
        return media.createMockMedia();
    }
}
