package app.albums.giphy.mock;

import app.albums.giphy.controller.api.SlackButtonEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Profile("test")
public class MainMock {

    private Map<String, SlackButtonEvent.User> users = new HashMap<>();

    public SlackButtonEvent.User addUser(SlackButtonEvent.User user) {
        users.put(user.getId(), user);
        return user;
    }

    public void cleanup() {
        users.clear();
    }

}
