package app.albums.giphy.builders;

import app.albums.giphy.controller.api.SlackButtonEvent;
import app.albums.giphy.controller.api.SlackSlashEvent;
import app.albums.giphy.service.SlackService;
import app.albums.giphy.util.StringUtility;
import app.albums.giphy.util.TestActionBuilder;

public class CreateSlackSlashEventBuilder implements TestActionBuilder<Void> {

    private final SlackService slackService;
    private String userId = StringUtility.createRandomCharString(8);
    private String channelName = "#" + StringUtility.createRandomString(6);
    private String text;

    public CreateSlackSlashEventBuilder(SlackService slackService) {
        this.slackService = slackService;
    }

    public CreateSlackSlashEventBuilder text(String text) {
        this.text = text;
        return this;
    }

    public CreateSlackSlashEventBuilder channel(String channelName) {
        this.channelName = channelName;
        return this;
    }

    public CreateSlackSlashEventBuilder userId(SlackButtonEvent.User user) {
        this.userId = user.getId();
        return this;
    }

    @Override
    public Void build() {
        slackService.handleSlashEvent(
                new SlackSlashEvent()
                        .setText(text)
                        .setUserId(userId)
                        .setChannelName(channelName)
        );


        return empty();
    }
}
