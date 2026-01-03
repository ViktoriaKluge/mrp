package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.dto.sql.SQLLikeDto;
import at.technikum.application.dto.sql.SQLRatingDto;
import at.technikum.application.enums.Stars;
import at.technikum.application.enums.UserType;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.NotAuthorizedException;
import at.technikum.application.model.Like;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;
import at.technikum.application.service.RatingService;
import at.technikum.server.http.*;

import java.util.List;
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

        if (path.length == 2 && method == Method.GET) {
            List<SQLRatingDto> allRatings = this.ratingService.allRatings();
            return json(allRatings,Status.OK);
        }

        if (path.length > 2) {
            UUID id = UUID.fromString(path[2]);
            Rating rating = this.ratingService.findById(id);
            User user = requestDto.getUser();
            User creator = rating.getCreator();

            if (method.equals(Method.POST)) {

                if (path[3].equals("like") && authorizedOther(user, creator)) {
                    return like(rating,user);
                }

                if (path[3].equals("confirm")) {
                    if (isAdmin(user)){
                        return confirm(rating);
                    }
                }
            }

            if (authorizedSame(user, creator)){
                if (method.equals(Method.PUT)) {
                    Rating update = toOtherObject(requestDto, Rating.class);
                    update.setStars(Stars.fromValue(requestDto.getStars())); // mapper mapt ordinal
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
        SQLRatingDto updated = this.ratingService.update(old,update);
        return json(updated, Status.OK);
    }

    private Response confirm(Rating rating) {
        SQLRatingDto confirmed = this.ratingService.confirm(rating);
        return json(confirmed, Status.OK);
    }

    private Response like(Rating rating, User user) {
        Like like = new Like(rating,user);
        SQLLikeDto liked = this.ratingService.like(like);
        return json(liked, Status.OK);
    }

    private boolean authorizedSame (User user, User creator) {
        if (user.getId().equals(creator.getId())) {
            return true;
        }
        throw new NotAuthorizedException("Only the creator can modify their ratings");
    }

    private boolean authorizedOther (User user, User creator) {
        if (!user.getId().equals(creator.getId())) {
            return true;
        }
        throw new NotAuthorizedException("You can only like ratings from other users");
    }

    public boolean isAdmin(User user) {
        if (user.getUserType() == UserType.Admin){
            return true;
        }
        throw new NotAuthorizedException("You need to be an admin to do this");
    }
}
