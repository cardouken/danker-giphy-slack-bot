package app.albums.giphy.rest.api.giphy;

import app.albums.giphy.rest.api.giphy.pojo.GiphyResults;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GiphyRandomResponse {

    private GiphyResults.Data data;

    public GiphyResults.Data getData() {
        return data;
    }

}
