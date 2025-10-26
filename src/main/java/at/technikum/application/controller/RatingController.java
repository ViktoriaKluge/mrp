package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.service.RatingService;
import at.technikum.server.http.*;

public class RatingController extends Controller {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public Response handle(Request request) {
        String[] path = request.getPath().split("/");
        String method = request.getMethod();
        String body = request.getBody();

        if (method.equals(Method.POST.getVerb())) {
            if(path[3].equals("like")){
                return like(path[2]);
            }
            if(path[3].equals("confirm")){
                return confirm(path[2]);
            }
        }

        if (method.equals(Method.PUT.getVerb())) {
            return putRating(path[2]);
        }

        if (method.equals(Method.DELETE.getVerb())) {
            return delete(path[2]);
        }

        throw new EntityNotFoundException("Path not found");
    }

    private Response delete(String s) {
        return null;
    }

    private Response putRating(String s) {
        return null;
    }

    private Response confirm(String s) {
        return null;
    }

    private Response like(String s) {
        return null;
    }
}
