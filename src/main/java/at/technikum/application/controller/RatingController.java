package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.service.RatingService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class RatingController extends Controller {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public Response handle(Request request) {
        String path = request.getPath();
        String method = request.getMethod();
        String body = request.getBody();

        Response response = new Response();
        return  response;
    }
}
