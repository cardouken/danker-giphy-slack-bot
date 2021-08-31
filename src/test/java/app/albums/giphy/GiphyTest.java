package app.albums.giphy;

import app.albums.giphy.rest.api.giphy.GiphyClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GiphyTest extends BaseTest {

    @Autowired
    private GiphyClient giphyClient;

    @Test
    public void test() {
        giphyClient.search("cardo");
    }
}