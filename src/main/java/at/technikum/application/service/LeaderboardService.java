package at.technikum.application.service;

import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.LeaderboardEntry;
import at.technikum.application.model.User;
import at.technikum.application.repository.UserRepository;

import java.util.List;

public class LeaderboardService {
    private final UserRepository userRepository;
    private List<User> users;

    public LeaderboardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<LeaderboardEntry> showLeaderboard() {
        return List.of();
    }

    public List<User> getAll() {
        this.users = this.userRepository.userList();
        if (users.isEmpty()) {
            throw new EntityNotFoundException("Users not found");
        }
        return users;
    }
}
