package app.albums.giphy.controller.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackListenerEvent {

    private Event event;
    private String challenge;

    public Event getEvent() {
        return event;
    }

    public SlackListenerEvent setEvent(Event event) {
        this.event = event;
        return this;
    }

    public String getChallenge() {
        return challenge;
    }

    public SlackListenerEvent setChallenge(String challenge) {
        this.challenge = challenge;
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event {

        private String type;
        private Item item;
        private String reaction;

        public String getType() {
            return type;
        }

        public Event setType(String type) {
            this.type = type;
            return this;
        }

        public Item getItem() {
            return item;
        }

        public Event setItem(Item item) {
            this.item = item;
            return this;
        }

        public String getReaction() {
            return reaction;
        }

        public Event setReaction(String reaction) {
            this.reaction = reaction;
            return this;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {

        private String channel;
        private String ts;

        public String getChannel() {
            return channel;
        }

        public Item setChannel(String channel) {
            this.channel = channel;
            return this;
        }

        public String getTs() {
            return ts;
        }

        public Item setTs(String ts) {
            this.ts = ts;
            return this;
        }
    }

}
