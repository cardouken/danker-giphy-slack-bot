package app.albums.giphy.builders;

import app.albums.giphy.controller.api.SlackButtonEvent;
import app.albums.giphy.mock.MainMock;
import app.albums.giphy.util.StringUtility;
import app.albums.giphy.util.TestActionBuilder;

public class CreateUserBuilder implements TestActionBuilder<SlackButtonEvent.User> {

    private final MainMock mainMock;
    private String userId = StringUtility.createRandomCharString(8);
    private String username = StringUtility.createRandomCharString(5);

    public CreateUserBuilder(MainMock mainMock) {
        this.mainMock = mainMock;
    }

    public CreateUserBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public CreateUserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public SlackButtonEvent.User build() {
        return mainMock.addUser(
                new SlackButtonEvent.User()
                        .setId(userId)
                        .setUsername(username)

        );
    }
}
