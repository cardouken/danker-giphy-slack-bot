package app.albums.giphy.controller.api;

public class SlackResponse {

    private String message;

    public SlackResponse() {
    }

    public SlackResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public SlackResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}
