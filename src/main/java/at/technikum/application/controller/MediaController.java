package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.Media;
import at.technikum.application.model.User;
import at.technikum.application.service.FavoritesService;
import at.technikum.application.service.MediaService;
import at.technikum.application.service.UserService;
import at.technikum.server.http.*;

import java.util.List;

public class MediaController extends Controller {

    private final MediaService mediaService;
    private final FavoritesService favoritesService;

    public MediaController(MediaService mediaService, FavoritesService favoritesService) {
        this.mediaService = mediaService;
        this.favoritesService = favoritesService;
    }

    @Override
    public Response handle(RequestDto requestDto) {
        String[] path = requestDto.getPath();
        Method method = requestDto.getMethod();

        if (method.equals(Method.GET)) {
            if (path.length==2) {
                return allMedia();
            }

            return mediaByID(path[2]);
        }

        if (method.equals(Method.POST)) {
            if (path.length==2) {
                return create("body");
            }
            if(path[3].equals("rate")){
                return null;
            }
            if(path[3].equals("favorite")){
                return addFavorite(path[2]);
            }
        }

        if (method.equals(Method.PUT)) {
            return update(path[2]);
        }

        if (method.equals(Method.DELETE)) {
            if(path[3].equals("favorite")){
                return deleteFavorite(path[2]);
            }
            return delete(path[2]);
        }

        throw new EntityNotFoundException("Path not found");
    }

    private Response deleteFavorite(String id) {
        String title = this.favoritesService.delete(id);
        Response response = new Response();
        response.setBody(title+" deleted");
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        return response;
    }

    private Response addFavorite(String id) {
        Media media = this.favoritesService.add(id);

        return json(media,Status.OK);
    }

    private Response allMedia() {
        List<Media> mediaList = this.mediaService.findAll();

        return json(mediaList,Status.OK);
    }

    private Response mediaByID(String id) {
        Media media = this.mediaService.findById(id);

        return json(media,Status.OK);
    }

    private Response create(String body) {
        Response response = new Response();

        // body to media
        Media media = new Media();
        this.mediaService.create(media);

        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("New Media Created");

        return response;
    }

    private Response update(String body) {
        Response response = new Response();

        // body in media aufteilen
        String id = "mockup";
        Media media = new Media();

        this.mediaService.update(media);

        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("Media updated successfully");

        return response;
    }

    private Response delete(String id) {
        Response response = new Response();

        this.mediaService.delete(id);

        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("User deleted successfully");

        return response;
    }
}
