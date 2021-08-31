package app.albums.giphy.controller;

import app.albums.giphy.config.JsonUtility;
import app.albums.giphy.controller.api.SlackButtonCommand;
import app.albums.giphy.controller.api.SlackSlashCommand;
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
    public void receiveEvent(@RequestBody SlackSlashCommand command) {
        slackService.receiveEvent(command);
    }

    @PostMapping(value = "/button", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void handleButtonEvent(@RequestParam String payload) throws JsonProcessingException {
        slackService.handleButtonEvent(JsonUtility.getObjectMapper().readValue(payload, SlackButtonCommand.class));
    }
}
