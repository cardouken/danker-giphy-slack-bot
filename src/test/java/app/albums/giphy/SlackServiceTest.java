package app.albums.giphy;

import app.albums.giphy.controller.api.SlackButtonEvent;
import com.slack.api.methods.request.chat.ChatPostEphemeralRequest;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import org.junit.jupiter.api.Test;

public class SlackServiceTest extends BaseTest {

    @Test
    public void slash_event_post_ephemeral_message() {
        // given
        final String text = "welcome";
        final String channel = "#random";

        // when
        createSlashEvent(text).channel(channel).build();

        // then
        methodsClientMock.pop(ChatPostEphemeralRequest.ChatPostEphemeralRequestBuilder.class)
                .assertThat("arg$1", channel)
                .assertThatContains("arg$3[0].imageUrl", text);
    }

    @Test
    public void slash_event_post_send() {
        // given
        final SlackButtonEvent.User user = createUser().build();
        final String text = "welcome";
        final String channel = "#random";
        createSlashEvent(text).channel(channel).userId(user).build();
        methodsClientMock.pop(ChatPostEphemeralRequest.ChatPostEphemeralRequestBuilder.class);

        // when
        createButtonEvent(user).setChannelId(channel).send().build();

        // then
        methodsClientMock.pop(ChatPostMessageRequest.ChatPostMessageRequestBuilder.class)
                .print();

    }
}
