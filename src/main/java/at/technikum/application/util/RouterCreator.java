package at.technikum.application.util;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.common.Router;
import at.technikum.application.controller.*;
import at.technikum.application.repository.*;
import at.technikum.application.service.*;

public class RouterCreator {
    private UserRepository userRepository;
    private MemoryMediaRepository memoryMediaRepository;
    private Router router;
    private final ConnectionPool connectionPool;

    public RouterCreator() {
        this.router = new Router();
        this.connectionPool = new ConnectionPool(
                "postgresql",
                "localhost",
                5432,
                "mrpUser",
                "mrp-pw",
                "mrp"
        );
        this.userRepository= new DbUserRepository(connectionPool);
        this.memoryMediaRepository = new MemoryMediaRepository();
        this.router.addRoute("/users", new UserController(new UserService(this.userRepository),
                new AuthService(this.userRepository),new RecommendationService(this.userRepository)));
        this.router.addRoute("/media", new MediaController( new MediaService(this.memoryMediaRepository),
                new FavoritesService(this.memoryMediaRepository)));
        this.router.addRoute("/rating", new RatingController( new RatingService(new MemoryRatingRepository())));
        this.router.addRoute("/leaderboard", new LeaderboardController(new LeaderboardService(this.userRepository)));
    }

    public Router getRouter() {

        return router;
    }

    public UserRepository getUserRepository() {
        return userRepository;
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