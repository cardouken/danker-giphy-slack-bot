package app.albums.giphy;

import app.albums.giphy.builders.CreateSlackButtonEventBuilder;
import app.albums.giphy.builders.CreateSlackSlashEventBuilder;
import app.albums.giphy.builders.CreateUserBuilder;
import app.albums.giphy.controller.api.SlackButtonEvent;
import app.albums.giphy.mock.GiphyClientMock;
import app.albums.giphy.mock.MainMock;
import app.albums.giphy.mock.MethodsClientMock;
import app.albums.giphy.service.SlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = GiphyApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(BaseTest.TEST_PROFILE)
public class BaseTest {

    static final String TEST_PROFILE = "test";

    @Autowired
    protected MethodsClientMock methodsClientMock;

    @Autowired
    protected GiphyClientMock giphyClientMock;

    @Autowired
    private MainMock mainMock;

    @Autowired
    private SlackService slackService;

    public CreateSlackSlashEventBuilder createSlashEvent(String text) {
        return new CreateSlackSlashEventBuilder(slackService)
                .text(text);
    }

    public CreateUserBuilder createUser() {
        return new CreateUserBuilder(mainMock);
    }

    public CreateSlackButtonEventBuilder createButtonEvent(SlackButtonEvent.User user) {
        return new CreateSlackButtonEventBuilder(slackService)
                .setUser(user);
    }

}
