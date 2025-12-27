package at.technikum.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {
    public static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private Json() {}
}
