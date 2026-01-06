package at.technikum.application.util;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.common.Router;
import at.technikum.application.controller.*;
import at.technikum.application.repository.*;
import at.technikum.application.service.*;

public class RouterCreator {
    private UserRepository userRepository;
    private MediaRepository mediaRepository;
    private RatingRepository ratingRepository;
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
        this.mediaRepository = new DbMediaRespository(connectionPool, userRepository);
        this.ratingRepository = new DbRatingRepository(connectionPool, userRepository, mediaRepository);
        this.router.addRoute("/users", new UserController(new UserService(this.userRepository,
                this.ratingRepository, this.mediaRepository), new AuthService(this.userRepository),
                new RecommendationService(this.userRepository)));
        this.router.addRoute("/media", new MediaController( new MediaService(this.mediaRepository),
                new FavoritesService(this.mediaRepository), new RatingService(this.ratingRepository)));
        this.router.addRoute("/rating", new RatingController( new RatingService(this.ratingRepository)));
        this.router.addRoute("/leaderboard", new LeaderboardController(new LeaderboardService(this.userRepository)));
    }

    public Router getRouter() {

        return router;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
