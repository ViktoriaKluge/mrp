package at.technikum.application.util;

import at.technikum.application.exception.*;
import at.technikum.server.http.Status;

public class ExceptionMapperCreator {
    public ExceptionMapperCreator() {

    }

    public ExceptionMapper getExceptionMapper() {
        ExceptionMapper exceptionMapper = new ExceptionMapper();
        exceptionMapper.register(EntityNotFoundException.class, Status.NOT_FOUND);
        exceptionMapper.register(NotJsonBodyException.class, Status.BAD_REQUEST);
        exceptionMapper.register(JsonConversionException.class, Status.INTERNAL_SERVER_ERROR);
        exceptionMapper.register(UnprocessableEntityException.class, Status.BAD_REQUEST);
        exceptionMapper.register(NotAuthorizedException.class, Status.UNAUTHORIZED);
        exceptionMapper.register(IncompatiblePayloadTypeException.class, Status.BAD_REQUEST);
        exceptionMapper.register(SQLToObjectException.class, Status.INTERNAL_SERVER_ERROR);
        exceptionMapper.register(ObjectToSQLException.class, Status.INTERNAL_SERVER_ERROR);
        exceptionMapper.register(DatabaseConnectionException.class, Status.INTERNAL_SERVER_ERROR);
        exceptionMapper.register(UniqueViolationException.class, Status.BAD_REQUEST);
        return exceptionMapper;
    }
}
