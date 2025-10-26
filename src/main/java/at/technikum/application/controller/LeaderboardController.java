package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.service.LeaderboardService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class LeaderboardController extends Controller {
    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
       this.leaderboardService = leaderboardService;
    }

    @Override
    public Response handle(Request request) {
        return null;
    }
}
