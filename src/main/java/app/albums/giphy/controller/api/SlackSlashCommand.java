package app.albums.giphy.controller.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackSlashCommand {

    private String token;
    private String command;
    private String text;

    @JsonProperty("team_id")
    private String teamId;

    @JsonProperty("team_domain")
    private String teamDomain;

    @JsonProperty("channel_id")
    private String channelId;

    @JsonProperty("channel_name")
    private String channelName;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("response_url")
    private String responseUrl;

    public String getToken() {
        return token;
    }

    public SlackSlashCommand setToken(String token) {
        this.token = token;
        return this;
    }

    public String getCommand() {
        return command;
    }

    public SlackSlashCommand setCommand(String command) {
        this.command = command;
        return this;
    }

    public String getText() {
        return text;
    }

    public SlackSlashCommand setText(String text) {
        this.text = text;
        return this;
    }

    public String getTeamId() {
        return teamId;
    }

    public SlackSlashCommand setTeamId(String teamId) {
        this.teamId = teamId;
        return this;
    }

    public String getTeamDomain() {
        return teamDomain;
    }

    public SlackSlashCommand setTeamDomain(String teamDomain) {
        this.teamDomain = teamDomain;
        return this;
    }

    public String getChannelId() {
        return channelId;
    }

    public SlackSlashCommand setChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    public String getChannelName() {
        return channelName;
    }

    public SlackSlashCommand setChannelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public SlackSlashCommand setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public SlackSlashCommand setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getResponseUrl() {
        return responseUrl;
    }

    public SlackSlashCommand setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
        return this;
    }
}
