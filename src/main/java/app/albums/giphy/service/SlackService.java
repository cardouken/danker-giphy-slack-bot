package app.albums.giphy.service;

import app.albums.giphy.controller.api.SlackButtonCommand;
import app.albums.giphy.controller.api.SlackSlashCommand;
import app.albums.giphy.enums.ActionValue;
import app.albums.giphy.rest.api.giphy.GiphyClient;
import app.albums.giphy.rest.api.giphy.GiphyGifResponse;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatUpdateRequest;
import com.slack.api.model.block.ActionsBlock;
import com.slack.api.model.block.ImageBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.BlockElement;
import com.slack.api.model.block.element.ButtonElement;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class SlackService {

    private static final String TOKEN = "xoxb-4950056332-2435964690037-WMcmZG2f0i514yCnyJms7Y8f";
    private final Slack slack;
    private final GiphyClient giphyClient;

    public SlackService(GiphyClient giphyClient) {
        this.giphyClient = giphyClient;
        this.slack = Slack.getInstance();
    }

    public void receiveEvent(SlackSlashCommand command) {
        final String channel = command.getChannelName();
        final String text = command.getText();

        final GiphyGifResponse queryResponse = giphyClient.search(text);

        if (queryResponse.getData().size() < 1) {
            return;
        }

        Random random = new Random();
        int upperBound = queryResponse.getData().size();
        final String url = queryResponse.getData().get(random.nextInt(upperBound)).getImages().getDownsized().getUrl();

        constructMessage(text, url, channel);
    }

    public void handleButtonEvent(SlackButtonCommand command) {
        final String responseUrl = command.getResponseUrl();

        final List<SlackButtonCommand.Action> actions = command.getActions();
        if (actions.isEmpty()) {
            return;
        }

        final String actionValue = actions.get(0).getValue();
        switch (ActionValue.valueOf(actionValue)) {
            case SHUFFLE:
                break;
            case SEND:
                break;
            case CANCEL:
                slack.methods(SlackService.TOKEN).chatUpdate(ChatUpdateRequest.builder()..build());
        }

    }

    private void constructMessage(String query, String url, String channel) {
        final List<BlockElement> blockElements = new ArrayList<>();
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
                        .value("shuffle")
                        .build()
        );
        blockElements.add(
                ButtonElement.builder()
                        .text(new PlainTextObject("Cancel", false))
                        .value("cancel")
                        .build()
        );

        final List<LayoutBlock> layoutBlocks = new ArrayList<>();
        layoutBlocks.add(
                ImageBlock.builder()
                        .title(PlainTextObject.builder().text(query).build())
                        .altText("alt-text")
                        .imageUrl(url)
                        .build()
        );
        layoutBlocks.add(
                ActionsBlock.builder()
                        .elements(blockElements)
                        .build()
        );

        sendMessage(url, layoutBlocks, channel);
    }

    private void sendMessage(String url, List<LayoutBlock> layoutBlocks, String channel) {
        try {
            slack.methods(SlackService.TOKEN).chatPostMessage(req -> req
                    .channel(channel)
                    .unfurlLinks(true)
                    .unfurlMedia(true)
                    .blocks(Optional.ofNullable(layoutBlocks).orElse(null))
                    .text(url));
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException("Slack API connection fucked up");
        }
    }
}
