package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.service.RatingService;
import at.technikum.server.http.ContentType;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

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

        if (path.equals("/rating")) {
            Response response = new Response();
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody("Rating overview");

            return  response;
        }

        throw new EntityNotFoundException("Path not found");
    }
}
