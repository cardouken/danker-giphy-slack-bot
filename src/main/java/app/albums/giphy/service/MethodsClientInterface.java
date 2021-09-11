package app.albums.giphy.service;

import com.slack.api.RequestConfigurator;
import com.slack.api.methods.request.chat.ChatDeleteRequest;
import com.slack.api.methods.request.chat.ChatPostEphemeralRequest;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;

public interface MethodsClientInterface {

    void chatPostEphemeral(RequestConfigurator<ChatPostEphemeralRequest.ChatPostEphemeralRequestBuilder> req);

    void chatPostMessage(RequestConfigurator<ChatPostMessageRequest.ChatPostMessageRequestBuilder> req);

    void chatDelete(RequestConfigurator<ChatDeleteRequest.ChatDeleteRequestBuilder> req);

}
