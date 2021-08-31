package app.albums.giphy.controller;

import app.albums.giphy.config.util.JsonUtility;
import app.albums.giphy.controller.api.SlackButtonEvent;
import app.albums.giphy.controller.api.SlackEventResponse;
import app.albums.giphy.controller.api.SlackListenerEvent;
import app.albums.giphy.controller.api.SlackSlashEvent;
import app.albums.giphy.service.SlackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlackController {

    private final SlackService slackService;

    public SlackController(SlackService slackService) {
        this.slackService = slackService;
    }

    @PostMapping(value = "/slack", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void handleSlashEvent(@RequestBody SlackSlashEvent event) {
        slackService.handleSlashEvent(event);
    }

    @PostMapping(value = "/button", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void handleButtonEvent(@RequestParam String payload) throws JsonProcessingException {
        slackService.handleButtonEvent(JsonUtility.getObjectMapper().readValue(payload, SlackButtonEvent.class));
    }

    @PostMapping(value = "/event")
    public SlackEventResponse receiveEvent(@RequestBody SlackListenerEvent event) {
        return slackService.receiveEvent(event);
    }
}
