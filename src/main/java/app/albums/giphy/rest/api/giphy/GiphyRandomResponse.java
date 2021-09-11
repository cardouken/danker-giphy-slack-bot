package app.albums.giphy.rest.api.giphy;

import app.albums.giphy.rest.api.giphy.pojo.GiphyResults;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GiphyRandomResponse {

    private GiphyResults data;

    public GiphyResults getData() {
        return data;
    }

    public GiphyRandomResponse setData(GiphyResults data) {
        this.data = data;
        return this;
    }
}
