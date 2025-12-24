package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.model.Favorites;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;
import at.technikum.application.service.FavoritesService;
import at.technikum.application.service.MediaService;
import at.technikum.application.service.RatingService;
import at.technikum.application.service.UserService;
import at.technikum.server.http.*;
import jakarta.mail.internet.HeaderTokenizer;

import java.util.List;
import java.util.UUID;

public class MediaController extends Controller {

    private final MediaService mediaService;
    private final FavoritesService favoritesService;
    private final RatingService ratingService;

    public MediaController(MediaService mediaService, FavoritesService favoritesService,
                           RatingService ratingService) {
        this.mediaService = mediaService;
        this.favoritesService = favoritesService;
        this.ratingService = ratingService;
    }

    @Override
    public Response handle(RequestDto requestDto) {
        String[] path = requestDto.getPath();
        Method method = requestDto.getMethod();

        if (path.length == 2) {
            if (method.equals(Method.GET)) {
                return allMedia();
            } else if (method.equals(Method.POST)) {
                Media media = toOtherObject(requestDto, Media.class);
                media.setId(UUID.randomUUID());
                media.setCreator(requestDto.getUser());
                return createMedia(media);
            }
        } else {
            UUID mid = UUID.fromString(path[2]);
            Media media = this.mediaService.findById(mid);

            if (method.equals(Method.GET)) {
                return json(media, Status.OK);
            }

            User user = requestDto.getUser();

            if (method.equals(Method.POST)) {
                if (path[3].equals("rate")) {
                    Rating rating = toOtherObject(requestDto, Rating.class);
                    rating.setId(UUID.randomUUID());
                    rating.setRatedMedia(media);
                    rating.setCreator(user);
                    return createRating(rating);
                }
                if (path[3].equals("favorite")) {
                    Favorites favorites = new Favorites();
                    favorites.setId(UUID.randomUUID());
                    favorites.setMedia(media);
                    favorites.setUser(user);
                    return addFavorite(favorites);
                }
            }

            if (method.equals(Method.DELETE)) {
                if (path[3].equals("favorite")) {
                    Favorites favorites = new Favorites();
                    favorites.setUser(user);
                    favorites.setMedia(media);
                    return deleteFavorite(favorites);
                }
            }

            if (authorized(user.getId(), media.getCreator().getId())) {
                if (method.equals(Method.PUT)) {
                    Media mediaNew = toOtherObject(requestDto, Media.class);
                    mediaNew.setId(media.getId());
                    return update(mediaNew);
                }
                if (method.equals(Method.DELETE)) {
                    return deleteMedia(media);
                }
            }
        }

        throw new EntityNotFoundException("Path not found");
    }

    private Response deleteFavorite(Favorites favorites) {
        String deletedFav = this.favoritesService.delete(favorites);
        return text("Deleted "+ deletedFav +" as favorite", Status.OK);
    }

    private Response addFavorite(Favorites favorites) {
        Favorites addedFav = this.favoritesService.add(favorites);
        return json(addedFav, Status.CREATED);
    }

    private Response allMedia() {
        List<Media> mediaList = this.mediaService.findAll();
        return json(mediaList,Status.OK);
    }

    private Response createMedia(Media media) {
        media = this.mediaService.create(media);
        return json(media,Status.CREATED);
    }

    private Response createRating(Rating rating) {
        Rating rated = this.ratingService.create(rating);
        return json(rating, Status.CREATED);
    }

    private Response update(Media update) {
        update = this.mediaService.update(update);
        return json(update, Status.OK);
    }

    private Response deleteMedia(Media media) {
        String deletedMedia = this.mediaService.delete(media);
        return text("Deleted "+deletedMedia, Status.OK);
    }

    private boolean authorized (UUID loggedInUID, UUID objectUID) {
        return loggedInUID == objectUID;
    }
}
