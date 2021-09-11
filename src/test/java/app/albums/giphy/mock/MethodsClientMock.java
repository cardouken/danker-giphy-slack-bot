package app.albums.giphy.mock;

import app.albums.giphy.config.util.JsonUtility;
import app.albums.giphy.service.MethodsClientInterface;
import app.albums.giphy.util.ApiResponse;
import app.albums.giphy.util.AssertableObjectFactory;
import com.slack.api.RequestConfigurator;
import com.slack.api.methods.request.chat.ChatDeleteRequest;
import com.slack.api.methods.request.chat.ChatPostEphemeralRequest;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
@Profile("test")
public class MethodsClientMock implements MethodsClientInterface {

    private final AssertableObjectFactory assertableObjectFactory;
    private final Map<Class<?>, List<Object>> map = new LinkedHashMap<>();

    public MethodsClientMock() {
        this.assertableObjectFactory = new AssertableObjectFactory(JsonUtility.getNullMapper());
    }

    @Override
    public void chatPostEphemeral(RequestConfigurator<ChatPostEphemeralRequest.ChatPostEphemeralRequestBuilder> req) {
        map.computeIfAbsent(ChatPostEphemeralRequest.ChatPostEphemeralRequestBuilder.class, key -> new ArrayList<>()).add(req);
    }

    @Override
    public void chatPostMessage(RequestConfigurator<ChatPostMessageRequest.ChatPostMessageRequestBuilder> req) {
        map.computeIfAbsent(ChatPostMessageRequest.ChatPostMessageRequestBuilder.class, key -> new ArrayList<>()).add(req);
    }

    @Override
    public void chatDelete(RequestConfigurator<ChatDeleteRequest.ChatDeleteRequestBuilder> req) {
        map.computeIfAbsent(ChatDeleteRequest.ChatDeleteRequestBuilder.class, key -> new ArrayList<>()).add(req);

    }

    public <T> ApiResponse pop(Class<T> clazz) {
        List<Object> values = map.getOrDefault(clazz, List.of());
        if (values.isEmpty()) {
            return assertableObjectFactory.create(null);
        }

        T object = values.stream()
                .map(o -> (T) o)
                .findFirst()
                .orElse(null);
        values.remove(object);
        return assertableObjectFactory.create(object);
    }

    public void cleanup() {
        map.clear();
    }
}
