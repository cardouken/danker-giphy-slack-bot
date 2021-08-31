package app.albums.giphy.rest.api.giphy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Optional;

@Service
public class GiphyClient {

    private static final String BASE_URI = "https://api.giphy.com/v1/gifs/search";
    private static final String API_KEY = "teFOldg6ihfKGXP2TzvQm9cZ7imwxgUU";
    private final RestTemplate giphyRestTemplate;

    @Autowired
    public GiphyClient(RestTemplate giphyRestTemplate) {
        this.giphyRestTemplate = giphyRestTemplate;
    }

    public GiphyGifResponse search(String query) {
        final Map<String, String> requestParams = Map.ofEntries(
                Map.entry("api_key", API_KEY),
                Map.entry("q", query)
        );
        final URI uri = giphyRestTemplate.getUriTemplateHandler()
                .expand(BASE_URI + "?api_key={api_key}&q={q}", requestParams);

        final RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        final ResponseEntity<GiphyGifResponse> response = giphyRestTemplate.exchange(requestEntity, GiphyGifResponse.class);

        if (response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            throw new RuntimeException("Giphy request limit exceeded! Try again later.");
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(
                    MessageFormat.format(
                            "Unsuccessful response: {0} received from Calendarific at endpoint {1} with response: {2}",
                            response.getStatusCodeValue(),
                            uri,
                            response.getBody()
                    )
            );
        }
        return new GiphyGifResponse()
                .setData(Optional.ofNullable(response.getBody()).map(GiphyGifResponse::getData).orElse(null));
    }

}
