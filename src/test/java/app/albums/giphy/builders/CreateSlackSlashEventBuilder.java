package app.albums.giphy.builders;

import app.albums.giphy.controller.api.SlackSlashEvent;
import app.albums.giphy.service.SlackService;
import app.albums.giphy.util.StringUtility;
import app.albums.giphy.util.TestActionBuilder;

public class CreateSlackSlashEventBuilder implements TestActionBuilder<Void> {

    private final SlackService slackService;
    private final String userId = StringUtility.createRandomCharString(8);
    private final String channelName = "#" + StringUtility.createRandomString(6);
    private String text;

    public CreateSlackSlashEventBuilder(SlackService slackService) {
        this.slackService = slackService;
    }

    public CreateSlackSlashEventBuilder setText(String text) {
        this.text = text;
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
