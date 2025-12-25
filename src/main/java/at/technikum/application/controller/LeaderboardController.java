package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.LeaderboardEntry;
import at.technikum.application.model.User;
import at.technikum.application.service.LeaderboardService;
import at.technikum.server.http.Method;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

import java.util.List;

public class LeaderboardController extends Controller {
    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
       this.leaderboardService = leaderboardService;
    }

    @Override
    public Response handle(RequestDto requestDto) {
        String[] path = requestDto.getPath();
        Method method = requestDto.getMethod();

        if (path.length == 2) {
            if (method == Method.GET) {
                List<LeaderboardEntry> leaderboard = this.leaderboardService.showLeaderboard();
                return json(leaderboard, Status.OK);
            }
        }
        throw new EntityNotFoundException("Path not found");
    }
}
