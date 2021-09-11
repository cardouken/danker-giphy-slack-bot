package app.albums.giphy.service;

import com.slack.api.RequestConfigurator;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatDeleteRequest;
import com.slack.api.methods.request.chat.ChatPostEphemeralRequest;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("!test")
public class MethodsClientImpl implements MethodsClientInterface {

    private final MethodsClient slack;

    public MethodsClientImpl(MethodsClient slack) {
        this.slack = slack;
    }

    @Override
    public void chatPostEphemeral(RequestConfigurator<ChatPostEphemeralRequest.ChatPostEphemeralRequestBuilder> req) {
        try {
            slack.chatPostEphemeral(req);
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException("Slack API done goofed");
        }
    }

    @Override
    public void chatPostMessage(RequestConfigurator<ChatPostMessageRequest.ChatPostMessageRequestBuilder> req) {
        try {
            slack.chatPostMessage(req);
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException("Slack API done goofed");
        }
    }

    @Override
    public void chatDelete(RequestConfigurator<ChatDeleteRequest.ChatDeleteRequestBuilder> req) {
        try {
            slack.chatDelete(req);
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException("Slack API done goofed up or we don't have enough permissions to delete the message");
        }
    }
}
