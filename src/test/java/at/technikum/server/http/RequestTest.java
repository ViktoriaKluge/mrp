package at.technikum.server.http;

import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.exception.NotAuthorizedException;
import at.technikum.application.exception.NotJsonBodyException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    // positive
    // valid input, no body
    @Test
    void givenValidInput_whenGetRequestDto_thenReturnRequestDto() {
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setPath("/path/123");
        Map<String, List<String>> headers = Map.of(
                "Authorization",
                List.of("Bearer testToken")
        );
        request.setHeaders(headers);

        RequestDto dto = request.getRequestDto();

        assertNotNull(dto);
        assertEquals("testToken", dto.getToken());
        assertEquals("path", dto.getPath()[1]);
        assertEquals("123", dto.getPath()[2]);
    }

    // valid body
    @Test
    void givenValidBody_whenGetRequestDto_thenReturnRequestDto() {
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setPath("/path");
        Map<String, List<String>> headers = new HashMap<>();
        request.setHeaders(headers);
        request.setBody("{\"username\":\"alice\"}");

        RequestDto dto = request.getRequestDto();

        assertNotNull(dto);
        assertEquals("alice", dto.getUsername());
        assertEquals(Method.GET, dto.getMethod());
    }

    // valid input, empty header
    @Test
    void givenNoHeader_whenGetRequestDto_thenReturnRequestDto() {
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setPath("/path");
        Map<String, List<String>> headers = new HashMap<>();
        request.setHeaders(headers);

        RequestDto dto = request.getRequestDto();

        assertNotNull(dto);
    }

    // negative
    // invalid authorization header
    @Test
    void givenInvalidHeader_whenGetRequestDto_thenThrowException() {
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setPath("/path");
        Map<String, List<String>> headers = Map.of(
                "Authorization",
                List.of("Invalid testToken")
        );
        request.setHeaders(headers);

        assertThrows(NotAuthorizedException.class, ()->request.getRequestDto());
    }

    // bearer without token
    @Test
    void givenEmptyToken_whenGetRequestDto_thenThrowException() {
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setPath("/path");
        Map<String, List<String>> headers = Map.of(
                "Authorization",
                List.of("Bearer ")
        );
        request.setHeaders(headers);

        assertThrows(NotAuthorizedException.class, ()->request.getRequestDto());
    }

    // invalid body
    @Test
    void givenInvalidBody_whenGetRequestDto_thenThrowsException() {
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setPath("/path");
        Map<String, List<String>> headers = new HashMap<>();
        request.setHeaders(headers);
        request.setBody("no json body");

        assertThrows(NotJsonBodyException.class, ()->request.getRequestDto());
    }

}