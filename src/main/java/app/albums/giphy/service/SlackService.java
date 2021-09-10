package app.albums.giphy.service;

import app.albums.giphy.config.util.JsonUtility;
import app.albums.giphy.controller.api.SlackButtonEvent;
import app.albums.giphy.controller.api.SlackEventResponse;
import app.albums.giphy.controller.api.SlackListenerEvent;
import app.albums.giphy.controller.api.SlackSlashEvent;
import app.albums.giphy.enums.ActionType;
import app.albums.giphy.enums.ActionValue;
import app.albums.giphy.enums.EventType;
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
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class SlackService {

    private static final String BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");
    private static final int SWITCHEROO_CHANCE = 65; // % chance of the gif being replaced
    private static final List<String> TARGET_CHANNELS = List.of(
            "C04TY1NEN", // #general
            "C04TY1NEW", // #random
            "C02AXH70FGA" // #bot-playground
    );
    private static final Set<String> TARGET_KEYWORDS = Set.of(
            "bienvenue",
            "welcome",
            "hello",
            "bonjour"
    );
    private static final List<String> REPLACEMENT_KEYWORDS = List.of(
            "welcome to hell",
            "hasta la vista",
            "teletubbies",
            "trailer park boys"
    );

    private final Map<String, QueryUrlResult> userUrlMap = new HashMap<>();
    private final Slack slack;
    private final GiphyClient giphyClient;
    private final RestTemplate slackRestTemplate;
    private final Random randomizer;

    public SlackService(GiphyClient giphyClient, RestTemplate slackRestTemplate) {
        this.giphyClient = giphyClient;
        this.slackRestTemplate = slackRestTemplate;
        this.slack = Slack.getInstance();
        this.randomizer = new Random();
    }

    public void handleSlashEvent(SlackSlashEvent event) {
        final String query = event.getText();
        final String userId = event.getUserId();

        if (event.getText().contains("slack.com/archives")) {
            deleteMessageByPermalink(event.getText());
            return;
        }

        final String url = giphyClient.findRandomGifByQuery(query);
        userUrlMap.put(userId, new QueryUrlResult(query, url));

        final List<LayoutBlock> message = constructMessage(query, url, MessageType.EPHEMERAL);
        postEphemeralMessage(message, event.getChannelName(), userId);
    }

    public void handleButtonEvent(SlackButtonEvent event) {
        final String userId = event.getUser().getId();
        final String responseUrl = event.getResponseUrl();
        final String action = event.getAction().getValue();
        final String channelId = event.getChannel().getId();

        if (Objects.equals(action, ActionValue.SEND.name())) {
            final QueryUrlResult userQueryResult = userUrlMap.get(userId);
            final String userQuery = userQueryResult.getQuery();

            List<LayoutBlock> message = constructMessage(userQuery, userQueryResult.getUrl(), MessageType.CHANNEL);

            final int luck = randomizer.nextInt(100);
            if (isEligibleForSwitcheroo(channelId, userQuery, luck)) {
                final String replacementQuery = REPLACEMENT_KEYWORDS.get(randomizer.nextInt(REPLACEMENT_KEYWORDS.size()));
                final String replacementUrl = giphyClient.findRandomGifByQuery(replacementQuery);

                message = constructMessage(userQuery, replacementUrl, MessageType.CHANNEL);
                logResult(channelId, userQuery, luck, replacementQuery);
            }

            logResult(null, userQuery, luck, null);

            postChatMessage(message, channelId, responseUrl);
            return;
        }

        if (Objects.equals(action, ActionValue.CANCEL.name())) {
            postWebhook(responseUrl, ActionType.DELETE_ORIGINAL, null);
            return;
        }

        // User shuffled, get a new gif and replace the ephemeral message with a new one
        final String url = giphyClient.findRandomGifByQuery(action);
        userUrlMap.put(userId, new QueryUrlResult(action, url));
        final List<LayoutBlock> message = constructMessage(action, url, MessageType.EPHEMERAL);
        postWebhook(responseUrl, ActionType.REPLACE_ORIGINAL, message);
    }

    public SlackEventResponse receiveEvent(SlackListenerEvent listenerEvent) {
        if (listenerEvent.getChallenge() != null) {
            return new SlackEventResponse(listenerEvent.getChallenge());
        }

        final SlackListenerEvent.Event event = listenerEvent.getEvent();
        final SlackListenerEvent.Item item = event.getItem();
        if (event.getType() == EventType.REACTION_ADDED && Objects.equals(event.getReaction(), "monkas")) {
            deleteMessageByReaction(item.getChannel(), item.getTs());
        }

        return new SlackEventResponse();
    }

    private boolean isEligibleForSwitcheroo(String channelId, String userQuery, int luck) {
        if (luck > SWITCHEROO_CHANCE) {
            return TARGET_CHANNELS.contains(channelId) || TARGET_KEYWORDS.contains(userQuery);
        }
        return false;
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
            throw new RuntimeException("Slack API done goofed");
        }
    }

    private void postChatMessage(List<LayoutBlock> layoutBlocks, String channel, String responseUrl) {
        try {
            slack.methods(SlackService.BOT_TOKEN).chatPostMessage(req -> req
                    .channel(channel)
                    .unfurlMedia(true)
                    .text("sample text")
                    .username("giphy")
                    .blocks(Optional.ofNullable(layoutBlocks).orElse(null))
            );

            postWebhook(responseUrl, ActionType.DELETE_ORIGINAL, null);
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException("Slack API done goofed");
        }
    }

    private void deleteMessageByReaction(String channelId, String ts) {
        try {
            slack.methods(SlackService.BOT_TOKEN).chatDelete(req -> req
                    .channel(channelId)
                    .ts(String.valueOf(ts))
            );
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException("Slack API done goofed up or we don't have enough permissions to delete the message");
        }
    }

    private void deleteMessageByPermalink(String text) {
        final String[] channelAndTs = text.replace("https://onoffapp.slack.com/archives/", "").split("/");
        final String channel = channelAndTs[0];

        StringBuilder ts = new StringBuilder(channelAndTs[1].substring(1));
        ts.insert(ts.length() - 6, ".");
        try {
            slack.methods(SlackService.BOT_TOKEN).chatDelete(req -> req
                    .channel(channel)
                    .ts(String.valueOf(ts))
            );
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException("Slack API done goofed up or we don't have enough permissions to delete the message");
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

    private void logResult(String channelId, String userQuery, int luck, String replacementQuery) {
        if (channelId == null) {
            LogManager.getLogger(getClass())
                    .info(MessageFormat.format("Didn't replace keyword \"{0}\" because luck {1}% under threshold of {2}%",
                            userQuery,
                            luck,
                            SWITCHEROO_CHANCE
                    ));
            return;
        }

        LogManager.getLogger(getClass())
                .info(MessageFormat.format("Replacing keyword \"{0}\" with \"{1}\" because: luck: {2}% (over threshold of {3}%), in target channel: {4}, target keyword: {5}",
                        userQuery,
                        replacementQuery,
                        luck,
                        SWITCHEROO_CHANCE,
                        TARGET_CHANNELS.contains(channelId),
                        TARGET_KEYWORDS.contains(userQuery)
                ));
    }
}
