package at.technikum.application;

import at.technikum.application.ccc.AuthMiddleware;
import at.technikum.application.common.Application;
import at.technikum.application.common.ConnectionPool;
import at.technikum.application.common.Controller;
import at.technikum.application.common.Router;
import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.exception.*;
import at.technikum.application.util.ExceptionMapperCreator;
import at.technikum.application.util.RouterCreator;
import at.technikum.server.http.ContentType;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

import java.util.Arrays;

public class MrpApplication implements Application {

    private final Router router;
    private final ExceptionMapper exceptionMapper;
    private final AuthMiddleware authMiddleware;

    public MrpApplication() {
        RouterCreator routerCreator = new RouterCreator();
        ExceptionMapperCreator exceptionMapperCreator = new ExceptionMapperCreator();
        this.router = routerCreator.getRouter();
        this.exceptionMapper = exceptionMapperCreator.getExceptionMapper();
        this.authMiddleware = new AuthMiddleware(routerCreator.getUserRepository());
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
            /*
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);

            response.setBody(Arrays.toString(requestDto.getPath()));
            return response;

             */
            RequestDto requestDto = request.getRequestDto();
            requestDto.setId(authMiddleware.authenticate(requestDto));

            Controller controller = router.findController(request.getPath())
                    .orElseThrow(RuntimeException::new);


            return controller.handle(requestDto);


        } catch (Exception ex) {
            return exceptionMapper.toResponse(ex);
        }
    }
}
