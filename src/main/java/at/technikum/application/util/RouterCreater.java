package at.technikum.application.util;

import at.technikum.application.common.Router;
import at.technikum.application.controller.*;
import at.technikum.application.repository.*;
import at.technikum.application.service.*;

public class RouterCreater {
    public RouterCreater() {

    }

    public Router getRouter() {
        Router router = new Router();
        router.addRoute("/users", new UserController(new UserService(new MemoryUserRepository())));
        router.addRoute("/auth", new AuthController( new AuthService(new MemoryAuthRepository())));
        router.addRoute("/media", new MediaController( new MediaService(new MemoryMediaRepository())));
        router.addRoute("/rating", new RatingController( new RatingService(new MemoryRatingRepository())));
        router.addRoute("/favorites", new FavoritesController( new FavoritesService(new MemoryFavoritesRepository())));
        return router;
    }
}
