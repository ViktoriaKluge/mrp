package at.technikum.application.service;

import at.technikum.application.model.User;
import at.technikum.application.repository.MemoryUserRepository;

import java.util.List;

public class LeaderboardService {
    private final MemoryUserRepository memoryUserRepository;

    public LeaderboardService(MemoryUserRepository memoryUserRepository) {
        this.memoryUserRepository = memoryUserRepository;
    }

    public List<User> showLeaderboard() {
        return List.of();
    }
}
