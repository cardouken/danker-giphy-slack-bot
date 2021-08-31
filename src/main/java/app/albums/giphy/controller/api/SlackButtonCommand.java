package app.albums.giphy.controller.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackButtonCommand {

    private String type;
    private String token;

    @JsonProperty("action_ts")
    private String actionTs;

    @JsonProperty("message_ts")
    private String messageTs;

    @JsonProperty("attachment_id")
    private String attachmentId;

    @JsonProperty("callback_id")
    private String callbackId;

    @JsonProperty("trigger_id")
    private String triggerId;

    @JsonProperty("response_url")
    private String responseUrl;

    private List<Action> actions;

    private Team team;

    private Channel channel;

    private User user;

    public String getType() {
        return type;
    }

    public SlackButtonCommand setType(String type) {
        this.type = type;
        return this;
    }

    public String getToken() {
        return token;
    }

    public SlackButtonCommand setToken(String token) {
        this.token = token;
        return this;
    }

    public String getActionTs() {
        return actionTs;
    }

    public SlackButtonCommand setActionTs(String actionTs) {
        this.actionTs = actionTs;
        return this;
    }

    public String getMessageTs() {
        return messageTs;
    }

    public SlackButtonCommand setMessageTs(String messageTs) {
        this.messageTs = messageTs;
        return this;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public SlackButtonCommand setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
        return this;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public SlackButtonCommand setCallbackId(String callbackId) {
        this.callbackId = callbackId;
        return this;
    }

    public String getTriggerId() {
        return triggerId;
    }

    public SlackButtonCommand setTriggerId(String triggerId) {
        this.triggerId = triggerId;
        return this;
    }

    public String getResponseUrl() {
        return responseUrl;
    }

    public SlackButtonCommand setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
        return this;
    }

    public List<Action> getActions() {
        return actions;
    }

    public SlackButtonCommand setActions(List<Action> actions) {
        this.actions = actions;
        return this;
    }

    public Team getTeam() {
        return team;
    }

    public SlackButtonCommand setTeam(Team team) {
        this.team = team;
        return this;
    }

    public Channel getChannel() {
        return channel;
    }

    public SlackButtonCommand setChannel(Channel channel) {
        this.channel = channel;
        return this;
    }

    public User getUser() {
        return user;
    }

    public SlackButtonCommand setUser(User user) {
        this.user = user;
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Action {

        private String name;
        private String value;
        private String type;

        public String getName() {
            return name;
        }

        public Action setName(String name) {
            this.name = name;
            return this;
        }

        public String getValue() {
            return value;
        }

        public Action setValue(String value) {
            this.value = value;
            return this;
        }

        public String getType() {
            return type;
        }

        public Action setType(String type) {
            this.type = type;
            return this;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Team {

        private String id;
        private String domain;

        public String getId() {
            return id;
        }

        public Team setId(String id) {
            this.id = id;
            return this;
        }

        public String getDomain() {
            return domain;
        }

        public Team setDomain(String domain) {
            this.domain = domain;
            return this;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Channel {

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public Channel setId(String id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public Channel setName(String name) {
            this.name = name;
            return this;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {

        private String id;
        private String user;
        private String username;

        public String getId() {
            return id;
        }

        public User setId(String id) {
            this.id = id;
            return this;
        }

        public String getUser() {
            return user;
        }

        public User setUser(String user) {
            this.user = user;
            return this;
        }

        public String getUsername() {
            return username;
        }

        public User setUsername(String username) {
            this.username = username;
            return this;
        }
    }

}
