package app.albums.giphy.rest.api.giphy;

public interface GiphyClient {

    String getRandomGifUrlByQuery(String query);

    String getRandomGifUrl();

}
