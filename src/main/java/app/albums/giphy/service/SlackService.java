package app.albums.giphy.service;

import app.albums.giphy.config.JsonUtility;
import app.albums.giphy.controller.api.SlackButtonCommand;
import app.albums.giphy.controller.api.SlackSlashCommand;
import app.albums.giphy.enums.ActionValue;
import app.albums.giphy.pojo.UserQueryResults;
import app.albums.giphy.rest.api.giphy.GiphyClient;
import app.albums.giphy.rest.api.giphy.GiphyGifResponse;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.block.ActionsBlock;
import com.slack.api.model.block.ImageBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.BlockElement;
import com.slack.api.model.block.element.ButtonElement;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class SlackService {

    private static final String TOKEN = "xoxb-4950056332-2435964690037-WMcmZG2f0i514yCnyJms7Y8f";
    private final Map<String, UserQueryResults> userUrlMap = new HashMap<>();
    private final Slack slack;
    private final GiphyClient giphyClient;
    private final RestTemplate slackRestTemplate;

    public SlackService(GiphyClient giphyClient, RestTemplate slackRestTemplate) {
        this.giphyClient = giphyClient;
        this.slackRestTemplate = slackRestTemplate;
        this.slack = Slack.getInstance();
    }

    public void receiveEvent(SlackSlashCommand command) {
        final String channel = command.getChannelName();
        final String query = command.getText();
        final String userId = command.getUserId();

        final String url = getGifUrl(query);
        userUrlMap.put(userId, new UserQueryResults(query, url));

        final List<LayoutBlock> message = constructMessage(query, url, true);
        postEphemeralMessage(message, channel, userId);
    }

    public void handleButtonEvent(SlackButtonCommand command) {
        final String userId = command.getUser().getId();
        final String responseUrl = command.getResponseUrl();
        final List<SlackButtonCommand.Action> actions = command.getActions();
        if (actions.isEmpty()) {
            return;
        }

        final String actionValue = actions.get(0).getValue();
        if (!Objects.equals(actionValue, "send") && !Objects.equals(actionValue, "cancel")) {
            final String url = getGifUrl(actionValue);
            userUrlMap.put(userId, new UserQueryResults(actionValue, url));
            final List<LayoutBlock> message = constructMessage(actionValue, url, true);
            postWebhook(responseUrl, "replace_original", message);
            return;
        }

        switch (ActionValue.valueOf(actionValue.toUpperCase())) {
            case SEND:
                final UserQueryResults userQueryResults = userUrlMap.get(userId);
                final List<LayoutBlock> message = constructMessage(userQueryResults.getQuery(), userQueryResults.getUrl(), false);
                postChatMessage(message, command.getChannel().getId());
                postWebhook(responseUrl, "delete_original", null);
                break;
            case CANCEL:
                postWebhook(responseUrl, "delete_original", null);
                break;
            default:
                break;
        }
    }

    private String getGifUrl(String query) {
        final GiphyGifResponse queryResponse = giphyClient.search(query);
        if (queryResponse.getData().size() < 1) {
            throw new RuntimeException("no gifs found");
        }

        int bound = queryResponse.getData().size();
        return queryResponse.getData().get(new Random().nextInt(bound)).getImages().getDownsized().getUrl();
    }

    private void postWebhook(String responseUrl, String action, List<LayoutBlock> message) {
        final Map<String, Object> body = new HashMap<>();
        body.put(action, "true");

        if (message != null) {
            body.put("blocks", message);
        }

        final URI uri = slackRestTemplate.getUriTemplateHandler()
                .expand(responseUrl);

        HttpEntity<String> request = new HttpEntity<>(JsonUtility.toJson(body));
        slackRestTemplate.exchange(uri, HttpMethod.POST, request, String.class);
    }

    private List<LayoutBlock> constructMessage(String query, String url, boolean isEphemeral) {
        final List<LayoutBlock> layoutBlocks = new ArrayList<>();
        List<BlockElement> blockElements = new ArrayList<>();
        layoutBlocks.add(
                ImageBlock.builder()
                        .title(PlainTextObject.builder().text(query).build())
                        .altText("alt-text")
                        .imageUrl(url)
                        .build()
        );

        if (isEphemeral) {
            blockElements.add(
                    ButtonElement.builder()
                            .style("primary")
                            .text(new PlainTextObject("Send", false))
                            .value("send")
                            .build()
            );
            blockElements.add(
                    ButtonElement.builder()
                            .text(new PlainTextObject("Shuffle", false))
                            .value(query)
                            .build()
            );
            blockElements.add(
                    ButtonElement.builder()
                            .text(new PlainTextObject("Cancel", false))
                            .value("cancel")
                            .build()
            );

            layoutBlocks.add(
                    ActionsBlock.builder()
                            .elements(blockElements)
                            .build()
            );
        }

        return layoutBlocks;
    }

    private void postEphemeralMessage(List<LayoutBlock> layoutBlocks, String channel, String userId) {
        try {
            slack.methods(SlackService.TOKEN).chatPostEphemeral(req -> req
                    .channel(channel)
                    .user(userId)
                    .text("sample text")
                    .blocks(Optional.ofNullable(layoutBlocks).orElse(null))
            );
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException("Slack API connection fucked up");
        }
    }

    private void postChatMessage(List<LayoutBlock> layoutBlocks, String channel) {
        try {
            slack.methods(SlackService.TOKEN).chatPostMessage(req -> req
                    .channel(channel)
                    .unfurlMedia(true)
                    .text("sample text")
                    .blocks(Optional.ofNullable(layoutBlocks).orElse(null))
            );
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException("Slack API connection fucked up");
        }
    }
}
