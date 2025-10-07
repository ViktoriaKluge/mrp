package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.model.Media;
import at.technikum.application.model.User;
import at.technikum.application.service.MediaService;
import at.technikum.application.service.UserService;
import at.technikum.server.http.*;

public class MediaController extends Controller {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @Override
    public Response handle(Request request) {
        String path = request.getPath();
        String method = request.getMethod();
        String body = request.getBody();

        if (method.equals(Method.GET.getVerb())) {
            if (path.equals("/media")) {
                return allMedia();
            }

            return mediaByID(body);
        }

        if (method.equals(Method.POST.getVerb())) {
            return create(body);
        }

        if (method.equals(Method.PUT.getVerb())) {
            return update(body);
        }

        if (method.equals(Method.DELETE.getVerb())) {
            return delete(body);
        }

        // return notFound() ? oder Exception?
        throw new RuntimeException("404");
    }

    private Media findMedia(String id) {
        return this.mediaService.findById(id);
    }

    private Response allMedia() {
        Response response = new Response();

        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody(this.mediaService.findAll().toString());

        return response;
    }

    private Response mediaByID(String id) {
        Response response = new Response();

        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody(this.findMedia(id).toString());

        return response;
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
