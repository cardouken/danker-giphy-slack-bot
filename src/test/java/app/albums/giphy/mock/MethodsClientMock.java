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

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

@Component
@Profile("test")
public class MethodsClientMock implements MethodsClientInterface {

    private final List<Object> list = new LinkedList<>();
    private final AssertableObjectFactory assertableObjectFactory;

    public MethodsClientMock() {
        this.assertableObjectFactory = new AssertableObjectFactory(JsonUtility.getNullMapper());
    }

    @Override
    public void chatPostEphemeral(RequestConfigurator<ChatPostEphemeralRequest.ChatPostEphemeralRequestBuilder> req) {
        list.add(req);
    }

    @Override
    public void chatPostMessage(RequestConfigurator<ChatPostMessageRequest.ChatPostMessageRequestBuilder> req) {
        list.add(req);
    }

    @Override
    public void chatDelete(RequestConfigurator<ChatDeleteRequest.ChatDeleteRequestBuilder> req) {
        list.add(req);
    }

    public <T> ApiResponse popEphemeral(Predicate<T> predicate) {
        if (list.isEmpty()) {
            return assertableObjectFactory.create(null);
        }

        T object = list.stream()
                .map(o -> (T) o)
                .filter(predicate)
                .findFirst()
                .orElse(null);
        list.remove(object);
        return assertableObjectFactory.create(object);
    }

    public void cleanup() {
        list.clear();
    }
}
