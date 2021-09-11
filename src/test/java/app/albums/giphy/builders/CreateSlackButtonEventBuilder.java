package app.albums.giphy.builders;

import app.albums.giphy.controller.api.SlackButtonEvent;
import app.albums.giphy.enums.ActionValue;
import app.albums.giphy.service.SlackService;
import app.albums.giphy.util.StringUtility;
import app.albums.giphy.util.TestActionBuilder;

import java.util.List;

public class CreateSlackButtonEventBuilder implements TestActionBuilder<Void> {

    private final SlackService slackService;
    private final String responseUrl = "https://random.dank/" + StringUtility.createRandomString(5);
    private SlackButtonEvent.User user;
    private String action;
    private String channelId = "#" + StringUtility.createRandomCharString(5);


    public CreateSlackButtonEventBuilder(SlackService slackService) {
        this.slackService = slackService;
    }

    public CreateSlackButtonEventBuilder setUser(SlackButtonEvent.User user) {
        this.user = user;
        return this;
    }

    public CreateSlackButtonEventBuilder setChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    public CreateSlackButtonEventBuilder send() {
        this.action = ActionValue.SEND.name();
        return this;
    }

    public CreateSlackButtonEventBuilder shuffle() {
        this.action = ActionValue.SHUFFLE.name();
        return this;
    }

    public CreateSlackButtonEventBuilder cancel() {
        this.action = ActionValue.CANCEL.name();
        return this;
    }

    @Override
    public Void build() {
        slackService.handleButtonEvent(
                new SlackButtonEvent()
                        .setUser(user)
                        .setResponseUrl(responseUrl)
                        .setActions(List.of(new SlackButtonEvent.Action().setValue(action)))
                        .setChannel(new SlackButtonEvent.Channel().setId(channelId))
        );

        return empty();
    }
}
