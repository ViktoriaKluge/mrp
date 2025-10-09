package at.technikum.application;

import at.technikum.application.common.Application;
import at.technikum.application.common.Controller;
import at.technikum.application.common.Router;
import at.technikum.application.controller.AuthController;
import at.technikum.application.controller.UserController;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.ExceptionMapper;
import at.technikum.application.repository.MemoryAuthRepository;
import at.technikum.application.repository.MemoryUserRepository;
import at.technikum.application.service.AuthService;
import at.technikum.application.service.UserService;
import at.technikum.server.http.ContentType;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

import java.util.Optional;

public class MrpApplication implements Application {

    private final Router router;
    private final ExceptionMapper exceptionMapper;

    public MrpApplication() {
        this.router = new Router();

        this.router.addRoute("/users", new UserController(new UserService(new MemoryUserRepository())));
        this.router.addRoute("/auth", new AuthController( new AuthService(new MemoryAuthRepository())));
        this.exceptionMapper = new ExceptionMapper();
        Response response = new Response();
        response.setStatus(Status.INTERNAL_SERVER_ERROR);
        response.setContentType(ContentType.TEXT_PLAIN);

        this.exceptionMapper.register(EntityNotFoundException.class, response);
    }

    @Override
    public Response handle(Request request) {
        Response response = new Response();

        if (request.getPath().equals("/")) {
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody("This is the home page");

            return response;
        }

        Optional<Controller> controllerOpt = this.router.findController(request.getPath());

        if (controllerOpt.isEmpty()) {
            response.setStatus(Status.NOT_FOUND);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody("Not Found controller");

            return response;
        }

        Controller controller = controllerOpt.get();


        return  controller.handle(request);


       /* -----------

        try {
            Controller controller = router.findController(request.getPath())
                    .orElseThrow(RuntimeException::new);

            return controller.handle(request);
        } catch (Exception ex) {
            // map exception to http response
        }


        return response;*/
    }
}
