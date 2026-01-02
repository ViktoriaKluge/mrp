package at.technikum.application.service;

import at.technikum.application.model.LeaderboardEntry;
import at.technikum.application.repository.UserRepository;

import java.util.List;

public class LeaderboardService {
    private final UserRepository userRepository;

    public LeaderboardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<LeaderboardEntry> showLeaderboard() {
        return this.userRepository.leaderboard();
    }
}
