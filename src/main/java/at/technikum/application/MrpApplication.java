package at.technikum.application;

import at.technikum.application.common.Application;
import at.technikum.application.common.Controller;
import at.technikum.application.common.Router;
import at.technikum.application.controller.*;
import at.technikum.application.exception.*;
import at.technikum.application.repository.*;
import at.technikum.application.service.*;
import at.technikum.application.util.ExceptionMapperCreater;
import at.technikum.application.util.RouterCreater;
import at.technikum.server.http.ContentType;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

public class MrpApplication implements Application {

    private final Router router;
    private final ExceptionMapper exceptionMapper;

    public MrpApplication() {
        RouterCreater routerCreater = new RouterCreater();
        ExceptionMapperCreater exceptionMapperCreater = new ExceptionMapperCreater();
        this.router = routerCreater.getRouter();
        this.exceptionMapper = exceptionMapperCreater.getExceptionMapper();
        // dependency injection?

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

        try {
            Controller controller = router.findController(request.getPath())
                    .orElseThrow(RuntimeException::new);

            return controller.handle(request);
        } catch (Exception ex) {
            return exceptionMapper.toResponse(ex);
        }
    }
}
