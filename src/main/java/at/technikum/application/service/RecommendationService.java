package at.technikum.application.service;

import at.technikum.application.dto.sql.SQLRecommendationDto;
import at.technikum.application.model.User;
import at.technikum.application.repository.UserRepository;

import java.util.List;

public class RecommendationService {
    private final UserRepository userRepository;

    public RecommendationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<SQLRecommendationDto> getRecommendations(User user) {
        return this.userRepository.recommendations(user);
    }
}
