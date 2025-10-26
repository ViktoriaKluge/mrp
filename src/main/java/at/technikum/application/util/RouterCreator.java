package at.technikum.application.util;

import at.technikum.application.common.Router;
import at.technikum.application.controller.*;
import at.technikum.application.repository.*;
import at.technikum.application.service.*;

public class RouterCreator {
    public RouterCreator() {

    }

    public Router getRouter() {
        Router router = new Router();
        MemoryUserRepository memoryUserRepository = new MemoryUserRepository();
        router.addRoute("/users", new UserController(new UserService(memoryUserRepository),
                new AuthService(memoryUserRepository),new RecommendationService(memoryUserRepository)));
        router.addRoute("/media", new MediaController( new MediaService(new MemoryMediaRepository())));
        router.addRoute("/rating", new RatingController( new RatingService(new MemoryRatingRepository())));
        router.addRoute("/leaderboard", new LeaderboardController(new LeaderboardService(memoryUserRepository)));
        return router;
    }
}
/*

    --> /users
    --> /media
    --> /ratings
    --> /leaderboard

    Auth
    post /users/register
    post /users/login

    User
    get /users/uid/profile auth
    put /users/uid/profile auth
    get /users/uid/ratings auth
    get /users/uid/favorites auth

    media
    get /media
    post /media auth
    delete /media/mid auth
    get /media/mid
    put /media/mid auth

    rating
    post /media/mid/rate auth
    post /ratings/rid/like auth
    put /ratings/rid auth
    delete /ratings/rid auth
    post /ratings/rid/confirm auth

    favorites
    post /media/mid/favorite auth
    delete /media/mid/favorite auth

    recommendation
    get /users/uid/recommendations auth

    leaderboard
    get /leaderboard


 */