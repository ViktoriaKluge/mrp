package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.service.FavoritesService;
import at.technikum.server.http.*;

public class FavoritesController extends Controller {

    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @Override
    public Response handle(Request request) {
        String method = request.getMethod();
        String body = request.getBody();

        if (method.equals(Method.POST.getVerb())) {
            return add(body);
        }

        if (method.equals(Method.DELETE.getVerb())) {
            return delete(body);
        }

        throw new EntityNotFoundException("Path not found");
    }

    private Response add(String body) {
        Response response = new Response();

        this.favoritesService.add(body);

        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("added Favorite successfully");

        return response;
    }

    private Response delete(String body) {
        Response response = new Response();

        this.favoritesService.delete(body);

        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("deleted Favorite successfully");

        return response;
    }
}
