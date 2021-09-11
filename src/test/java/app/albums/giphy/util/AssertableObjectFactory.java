package app.albums.giphy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class AssertableObjectFactory {
    private final ObjectMapper objectMapper;

    public AssertableObjectFactory() {
        this(new ObjectMapper());
    }

    public AssertableObjectFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ApiResponse create(Object obj) {
        String json = (String) Optional.ofNullable(obj).map((o) -> {
            try {
                return this.objectMapper.writeValueAsString(o);
            } catch (JsonProcessingException var3) {
                throw new RuntimeException(var3);
            }
        }).orElse("{}");
        return new ApiResponse(json);
    }
}