package app.albums.giphy.controller.api;

public class SlackEventResponse {

    private String challenge;

    public SlackEventResponse() {
    }

    public SlackEventResponse(String message) {
        this.challenge = message;
    }

    public String getChallenge() {
        return challenge;
    }

    public SlackEventResponse setChallenge(String challenge) {
        this.challenge = challenge;
        return this;
    }
}
