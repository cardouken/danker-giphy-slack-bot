package app.albums.giphy.service;

import app.albums.giphy.config.util.JsonUtility;
import app.albums.giphy.controller.api.SlackButtonEvent;
import app.albums.giphy.controller.api.SlackSlashEvent;
import app.albums.giphy.enums.ActionType;
import app.albums.giphy.enums.ActionValue;
import app.albums.giphy.enums.MessageType;
import app.albums.giphy.pojo.QueryUrlResult;
import app.albums.giphy.rest.api.giphy.GiphyClient;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.block.ActionsBlock;
import com.slack.api.model.block.ContextBlock;
import com.slack.api.model.block.ImageBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.BlockElement;
import com.slack.api.model.block.element.ButtonElement;
import com.slack.api.model.block.element.ImageElement;
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

@Service
public class SlackService {

    private static final String BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");

    private final Map<String, QueryUrlResult> userUrlMap = new HashMap<>();
    private final Slack slack;
    private final GiphyClient giphyClient;
    private final RestTemplate slackRestTemplate;

    public SlackService(GiphyClient giphyClient, RestTemplate slackRestTemplate) {
        this.giphyClient = giphyClient;
        this.slackRestTemplate = slackRestTemplate;
        this.slack = Slack.getInstance();
    }

    public void handleSlashEvent(SlackSlashEvent event) {
        final String query = event.getText();
        final String userId = event.getUserId();

        final String url = giphyClient.findRandomGifByQuery(query);
        userUrlMap.put(userId, new QueryUrlResult(query, url));

        final List<LayoutBlock> message = constructMessage(query, url, MessageType.EPHEMERAL);
        postEphemeralMessage(message, event.getChannelName(), userId);
    }

    public void handleButtonEvent(SlackButtonEvent event) {
        final String userId = event.getUser().getId();
        final String responseUrl = event.getResponseUrl();
        final String action = event.getActions().get(0).getValue();

        if (Objects.equals(action, ActionValue.SEND.name())) {
            final QueryUrlResult userQueryResult = userUrlMap.get(userId);
            final List<LayoutBlock> message = constructMessage(userQueryResult.getQuery(), userQueryResult.getUrl(), MessageType.CHANNEL);
            postChatMessage(message, event.getChannel().getId());
            postWebhook(responseUrl, ActionType.DELETE_ORIGINAL, null);
            return;
        }

        if (Objects.equals(action, ActionValue.CANCEL.name())) {
            postWebhook(responseUrl, ActionType.DELETE_ORIGINAL, null);
            return;
        }

        // User shuffled, get a new gif and replace the ephemeral message with the new one
        final String url = giphyClient.findRandomGifByQuery(action);
        userUrlMap.put(userId, new QueryUrlResult(action, url));
        final List<LayoutBlock> message = constructMessage(action, url, MessageType.EPHEMERAL);
        postWebhook(responseUrl, ActionType.REPLACE_ORIGINAL, message);
    }

    private void postWebhook(String responseUrl, ActionType action, List<LayoutBlock> message) {
        final Map<String, Object> body = new HashMap<>();
        body.put(action.name().toLowerCase(), "true");

        if (message != null) {
            body.put("blocks", message);
        }

        final URI uri = slackRestTemplate.getUriTemplateHandler().expand(responseUrl);
        final HttpEntity<String> request = new HttpEntity<>(JsonUtility.toJsonSnakeCase(body));
        slackRestTemplate.exchange(uri, HttpMethod.POST, request, String.class);
    }

    private void postEphemeralMessage(List<LayoutBlock> layoutBlocks, String channel, String userId) {
        try {
            slack.methods(SlackService.BOT_TOKEN).chatPostEphemeral(req -> req
                    .channel(channel)
                    .user(userId)
                    .text("sample text")
                    .blocks(Optional.ofNullable(layoutBlocks).orElse(null))
            );
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException("Slack API fucked up");
        }
    }

    private void postChatMessage(List<LayoutBlock> layoutBlocks, String channel) {
        try {
            slack.methods(SlackService.BOT_TOKEN).chatPostMessage(req -> req
                    .channel(channel)
                    .unfurlMedia(true)
                    .text("sample text")
                    .username("Giphy")
                    .iconUrl("https://ca.slack-edge.com/T04TY1N9S-UU2LZLZBN-f7c9fa36e6eb-512")
                    .iconEmoji(":yep:")
                    .blocks(Optional.ofNullable(layoutBlocks).orElse(null))
            );
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException("Slack API fucked up");
        }
    }

    private List<LayoutBlock> constructMessage(String query, String url, MessageType messageType) {
        final List<LayoutBlock> layoutBlocks = new ArrayList<>();
        List<BlockElement> blockElements = new ArrayList<>();
        layoutBlocks.add(
                ImageBlock.builder()
                        .title(PlainTextObject.builder().text(query).build())
                        .altText(query)
                        .imageUrl(url)
                        .build()
        );

        if (messageType == MessageType.EPHEMERAL) {
            blockElements.add(
                    ButtonElement.builder()
                            .style("primary")
                            .text(new PlainTextObject("Send", false))
                            .value("SEND")
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
                            .value("CANCEL")
                            .build()
            );

            layoutBlocks.add(
                    ActionsBlock.builder()
                            .elements(blockElements)
                            .build()
            );
        }

        if (messageType == MessageType.CHANNEL) {
            layoutBlocks.add(
                    ContextBlock.builder()
                            .elements(
                                    List.of(
                                            ImageElement.builder().imageUrl("https://uustal.ee/giphy.png").altText("Giphy").build(),
                                            MarkdownTextObject.builder().text("Posted using /giphy").build()))
                            .build()
            );
        }

        return layoutBlocks;
    }
}
