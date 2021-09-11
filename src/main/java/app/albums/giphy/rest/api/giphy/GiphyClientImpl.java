package app.albums.giphy.rest.api.giphy;

import app.albums.giphy.config.util.JsonUtility;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Profile("!test")
public class GiphyClientImpl implements GiphyClient {

    private static final String BASE_URI = "https://api.giphy.com/v1/gifs";
    private static final String API_KEY = System.getenv("GIPHY_API_KEY");
    private static final MapType MAP_TYPE = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Object.class);

    private final RestTemplate giphyRestTemplate;

    public GiphyClientImpl(RestTemplate giphyRestTemplate) {
        this.giphyRestTemplate = giphyRestTemplate;
    }

    @Override
    public String getRandomGifUrlByQuery(String query) {
        final GiphyQueryResponse results = executeGet("/search?&q={q}", Map.of("q", query), GiphyQueryResponse.class);
        if (results.getData().size() < 1) {
            return getRandomGifUrl();
        }

        final int resultCount = results.getData().size();
        return results.getData().get(new Random().nextInt(resultCount))
                .getImages()
                .getDownsized()
                .getUrl();
    }

    @Override
    public String getRandomGifUrl() {
        final GiphyRandomResponse results = executeGet("/random?&rating=g", Map.of(), GiphyRandomResponse.class);
        return results.getData().getImages().getOriginal().getUrl();
    }

    private <T> T executeGet(String endpoint, Object request, Class<T> responseClass) {
        final Map<String, Object> params = JsonUtility.getNullMapper().convertValue(request, MAP_TYPE);
        final URI uri = new UriTemplate(BASE_URI + endpoint + "&api_key=" + API_KEY).expand(params);
        final RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();

        return execute(endpoint, requestEntity, responseClass);
    }

    private <T, R> T execute(String endpoint, RequestEntity<R> requestEntity, Class<T> responseClass) {
        final ResponseEntity<T> exchange = giphyRestTemplate.exchange(requestEntity, responseClass);

        if (exchange.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            throw new RuntimeException("Giphy request limit exceeded! Try again later.");
        }
        if (!exchange.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(
                    MessageFormat.format(
                            "Unsuccessful response: {0} received from Giphy at endpoint {1} with response: {2}",
                            exchange.getStatusCodeValue(),
                            endpoint,
                            exchange.getBody()
                    )
            );
        }
        if (exchange.getBody() == null) {
            throw new RuntimeException("No body received in Giphy response!");
        }

        return exchange.getBody();
    }

}
