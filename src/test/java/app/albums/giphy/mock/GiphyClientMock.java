package app.albums.giphy.mock;

import app.albums.giphy.rest.api.giphy.GiphyClient;
import app.albums.giphy.util.StringUtility;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("test")
public class GiphyClientMock implements GiphyClient {

    private List<String> validQueryWords = new ArrayList<>();

    @Override
    public String getRandomGifUrlByQuery(String query) {
        return "https://media1.giphy.com/media/" + StringUtility.createRandomString(8) + "/" + query;
    }

    @Override
    public String getRandomGifUrl() {
        return "https://media1.giphy.com/media/" + StringUtility.createRandomString(8) + "/random";
    }

    public void addQueryWords(List<String> words) {
        validQueryWords.addAll(words);
    }
}
