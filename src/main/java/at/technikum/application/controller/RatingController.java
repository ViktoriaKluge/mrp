package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.enums.UserType;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.NotAuthorizedException;
import at.technikum.application.model.Like;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;
import at.technikum.application.service.RatingService;
import at.technikum.server.http.*;

import java.util.UUID;

public class RatingController extends Controller {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public Response handle(RequestDto requestDto) {
        String[] path = requestDto.getPath();
        Method method = requestDto.getMethod();

        if (path.length > 2) {
            UUID id = UUID.fromString(path[2]);
            Rating rating = this.ratingService.findById(id);
            User user = requestDto.getUser();
            User creator = rating.getCreator();

            if (method.equals(Method.POST)) {
                if (path[3].equals("like")) {
                    return like(rating,user);
                }

                if (path[3].equals("confirm")) {
                    if (isAdmin(user)){
                        return confirm(rating);
                    }
                }
            }

            if (authorized(user, creator)){
                if (method.equals(Method.PUT)) {
                    Rating update = toOtherObject(requestDto, Rating.class);
                    return updateRating(rating, update);
                }

                if (method.equals(Method.DELETE)) {
                    return delete(rating);
                }
            }
        }
        throw new EntityNotFoundException("Path not found");
    }

    private Response delete(Rating rating) {
        String deleted = this.ratingService.delete(rating);
        return text("Deleted rating for "+ deleted, Status.OK);
    }

    private Response updateRating(Rating old, Rating update) {
        update = this.ratingService.update(old,update);
        return json(update, Status.OK);
    }

    private Response confirm(Rating rating) {
        rating = this.ratingService.confirm(rating);
        return json(rating, Status.OK);
    }

    private Response like(Rating rating, User user) {
        Like like = new Like(rating,user);
        Like liked = this.ratingService.like(like);
        return json(liked, Status.OK);
    }

    private boolean authorized (User user, User creator) {
        if (user.getId() == creator.getId()) {
            return true;
        }
        throw new NotAuthorizedException("Only the creator can modify their ratings");
    }

    public boolean isAdmin(User user) {
        if (user.getUserType() == UserType.Admin){
            return true;
        }
        throw new NotAuthorizedException("You need to be an admin to do this");
    }
}
